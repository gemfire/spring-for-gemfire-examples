package dev.gemfire.queries;

import dev.gemfire.queries.domain.EmailAddress;
import dev.gemfire.queries.repository.CustomerRepository;
import dev.gemfire.queries.domain.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.CqEvent;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class QueryClient {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GemfireTemplate customerTemplate;

    @Qualifier("Customers")
    @Autowired
    private Region<Long, Customer> customers;

    private final AtomicInteger counter = new AtomicInteger(0);

    private static final Log logger = LogFactory.getLog(QueryClient.class);
    public static void main(String[] args) {
        new SpringApplicationBuilder(QueryClient.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {
            logger.info("--- Continuous Queries ---");
            continuousQueryWorkingCorrectly();
            logger.info("--- OQL Queries ---");
            oqlQueriesConfiguredCorrectly();
        };
    }

    @ContinuousQuery(name = "CustomerCQ", query = "SELECT * FROM /Customers", durable = true)
    public void handleEvent(CqEvent event) {
        logger.info("Received message for CQ 'CustomerCQ'" + event);
        counter.incrementAndGet();
    }

    public void oqlQueriesConfiguredCorrectly() {

        logger.info("Inserting 3 entries for keys: 1, 2, 3");
        Customer john = new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith");
        Customer frank = new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport");
        Customer jude = new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons");
        customerRepository.save(john);
        customerRepository.save(frank);
        customerRepository.save(jude);
        assert customers.keySetOnServer().size() == 3;

        Customer customer = customerRepository.findById(2L).get();
        assert customer ==  frank;
        logger.info("Find customer with key=2 using GemFireRepository: " + customer);
        List customerList = customerTemplate.find("select * from /Customers where id=$1", 2L).asList();
        assert customerList.size() == 1;
        assert customerList.contains(frank);
        logger.info("Find customer with key=2 using GemFireTemplate: " + customerList);

        customer = new Customer(1L, new EmailAddress("3@3.com"), "Jude", "Smith");
        customerRepository.save(customer);
        assert customers.keySetOnServer().size() == 3;

        customerList = customerRepository.findByEmailAddressUsingIndex("3@3.com");
        assert customerList.size() ==  2;
        assert customerList.contains(frank);
        assert customerList.contains(customer);
        logger.info("Find customers with emailAddress=3@3.com: " + customerList);

        customerList = customerRepository.findByFirstNameUsingIndex("Frank");
        assert customerList.get(0).equals(frank);

        logger.info("Find customers with firstName=Frank: " + customerList);

        customerList = customerRepository.findByFirstNameUsingIndex("Jude");

        assert customerList.size() == 2;
        assert customerList.contains(jude);
        assert customerList.contains(customer);

        logger.info("Find customers with firstName=Jude: " + customerList);
    }

    public void continuousQueryWorkingCorrectly() {

        assert this.customers.isEmpty();

        logger.info("Inserting 3 entries for keys: 1, 2, 3");

        customerRepository.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
        customerRepository.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
        customerRepository.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));

        assert customers.keySetOnServer().size() ==  3;

        Awaitility.await().atMost(30, TimeUnit.SECONDS).until(() -> this.counter.get() == 3);
    }
}
