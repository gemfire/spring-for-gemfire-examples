package dev.gemfire.transactions;

import dev.gemfire.transactions.domain.EmailAddress;
import dev.gemfire.transactions.service.CustomerService;
import dev.gemfire.transactions.domain.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionalClient {

    @Autowired
    private CustomerService customerService;

    private static final Log logger = LogFactory.getLog(TransactionalClient.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(TransactionalClient.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {
            logger.info("Number of Entries stored before = " + customerService.numberEntriesStoredOnServer());
            customerService.createFiveCustomers();
            assert customerService.numberEntriesStoredOnServer() ==  5;
            logger.info("Number of Entries stored after = " + customerService.numberEntriesStoredOnServer());
            logger.info("Customer for ID before (transaction commit success) = " + customerService.findById(2L).get());
            customerService.updateCustomersSuccess();
            assert customerService.numberEntriesStoredOnServer() ==  5;
            Customer customer = customerService.findById(2L).get();
            assert customer.firstName().equals("Humpty");
            logger.info("Customer for ID after (transaction commit success) = " + customer);

            try {
                customerService.updateCustomersFailure();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }

            customer = customerService.findById(2L).get();
            assert customer.firstName().equals("Humpty");
            logger.info("Customer for ID after (transaction commit failure) = " + customerService.findById(2L).get());

            Customer numpty = new Customer(2L, new EmailAddress("2@2.com"), "Numpty", "Hamilton");
            Customer frumpy = new Customer(2L, new EmailAddress("2@2.com"), "Frumpy", "Hamilton");
            customerService.updateCustomersWithDelay(1000, numpty);
            customerService.updateCustomersWithDelay(10, frumpy);
            customer = customerService.findById(2L).get();
            assert customer.equals(frumpy);
            logger.info("Customer for ID after 2 updates with delay = " + customer);
        };
    }
}
