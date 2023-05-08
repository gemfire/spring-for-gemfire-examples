/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.custom;

import dev.gemfire.expiration.custom.domain.Address;
import dev.gemfire.expiration.custom.domain.Customer;
import dev.gemfire.expiration.custom.domain.EmailAddress;
import dev.gemfire.expiration.custom.domain.Product;
import dev.gemfire.expiration.custom.repository.CustomerRepository;
import dev.gemfire.expiration.custom.repository.ProductRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.awaitility.Awaitility;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ExpirationClient {

	private static final Log logger = LogFactory.getLog(ExpirationClient.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(ExpirationClient.class).web(WebApplicationType.NONE).build().run(args);
	}

	@Bean
	public ApplicationRunner runner(CustomerRepository customerRepository, ProductRepository productRepository) {
		return args -> {
			logger.info("--- Cache Defined Expiration ---");
			cacheDefinedExpirationIsConfiguredCorrectly(customerRepository);

			logger.info("--- Custom Expiration ---");
			customExpirationIsConfiguredCorrectly(productRepository);
		};
	}

	public void cacheDefinedExpirationIsConfiguredCorrectly(CustomerRepository customerRepository) {

		customerRepository.save(new Customer(1L, new EmailAddress("address@website.domain"),
				"John", "Smith",
				new Address("711 Larch Ct", "Paris", "Alderaan")));

		System.out.printf("Starting TTL wait period: %s%n", Instant.now());
		// Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
		// will be used.
		Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(10, TimeUnit.SECONDS)
				.until(() -> customerRepository.findById(1L).isEmpty());

		System.out.printf("Ending TTL wait period: %s%n", Instant.now());

		customerRepository.save(new Customer(1L, new EmailAddress("email@address.com"),
				"Jane", "Doe",
				new Address("345 1st St", "Sparta", "Greece")));

		System.out.printf("Starting Idle wait period: %s%n", Instant.now());

		// Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
		Awaitility.await().pollDelay(2, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS)
				.atMost(10, TimeUnit.SECONDS).until(() -> customerRepository.findById(1L).isEmpty());

		System.out.printf("Ending Idle wait period: %s%n", Instant.now());
	}

	public void customExpirationIsConfiguredCorrectly(ProductRepository productRepository) {
		productRepository.save(new Product(3L, "MacBook Pro", BigDecimal.valueOf(20), "A cool computing device"));

		System.out.printf("Starting TTL wait period: %s%n", Instant.now());
		// Due to the constant "getting" of the entry, the idle expiry timeout will not be met and the time-to-live
		// will be used.
		Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(10, TimeUnit.SECONDS)
				.until(() -> productRepository.findById(3L).isEmpty());

		logger.info("Ending TTL wait period: " + Instant.now());

		productRepository.save(new Product(3L, "MacBook Pro", BigDecimal.valueOf(20), "A cool computing device"));

		System.out.printf("Starting Idle wait period: %s%n", Instant.now());

		// Due to the delay in "getting" the entry, the idle timeout of 2s should delete the entry.
		Awaitility.await().pollDelay(2, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS)
				.atMost(10, TimeUnit.SECONDS).until(() -> productRepository.findById(3L).isEmpty());

		System.out.printf("Ending Idle wait period: %s%n", Instant.now());
	}
}
