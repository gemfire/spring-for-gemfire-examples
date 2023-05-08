/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.entity;

import dev.gemfire.expiration.entity.domain.Address;
import dev.gemfire.expiration.entity.domain.Order;
import dev.gemfire.expiration.entity.repository.OrderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.awaitility.Awaitility;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class EntityDefinedExpirationClient {

    private static final Log logger = LogFactory.getLog(EntityDefinedExpirationClient.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(EntityDefinedExpirationClient.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public ApplicationRunner runner(OrderRepository orderRepository) {
        return args -> {
            logger.info("--- Entity Defined Expiration ---");
            orderRepository.save(new Order(1L, 50L,
                    new Address("123 Main St", "Metropolis", "Narnia")));

            logger.info("Starting TTL wait period: " + Instant.now());
            // Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
            // will be used.
            Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(10, TimeUnit.SECONDS)
                    .until(() -> orderRepository.findById(1L).isEmpty());

            logger.info("Ending TTL wait period: " + Instant.now());

            orderRepository.save(new Order(1L, 50L,
                    new Address("111 3rd Ave", "Townsville", "Mordor")));

            logger.info("Starting Idle wait period: " + Instant.now());

            // Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
            Awaitility.await().pollDelay(2, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS)
                    .atMost(10, TimeUnit.SECONDS).until(() -> orderRepository.findById(1L).isEmpty());

            logger.info("Ending Idle wait period: " + Instant.now());
        };
    }
}
