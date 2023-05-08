package dev.gemfire.eviction;

import dev.gemfire.eviction.domain.Address;
import dev.gemfire.eviction.domain.Order;
import dev.gemfire.eviction.repository.OrderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EvictionClient {

    private static final Log logger = LogFactory.getLog(EvictionClient.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(EvictionClient.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public ApplicationRunner runner(OrderRepository orderRepository, @Qualifier("Orders") Region<Long, Order> orderRegion) {
        return args -> {
            final int evictionThreshold = 10;
            logger.info("Inserting " + (evictionThreshold + 3) + " entries");
            for (long i = 0; i < evictionThreshold + 3; i++) {
                orderRepository.save(new Order(i, i,
                        new Address("123 Main St", "New York", "Spain")));
            }

            logger.info("Asserting that there are only " + evictionThreshold + " entries");
            assert orderRegion.size() == evictionThreshold;

            logger.info("Entries: " + orderRegion.size());

            orderRepository.deleteAll();
        };
    }
}
