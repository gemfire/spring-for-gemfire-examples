// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.intro;

import dev.gemfire.intro.model.Customer;
import dev.gemfire.intro.repo.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link SpringBootApplication Spring Boot application} implementing a Customer Relationship Management service (CRM).
*
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @since 1.2.0
 */
@SpringBootApplication
public class CrmApplication {

	private static final Logger log = LoggerFactory.getLogger(CrmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(CustomerRepository customerRepository) {

		return args -> {

			assertThat(customerRepository.count()).isEqualTo(0);

			Customer jonDoe = new Customer(1L, "JonDoe");

			log.info("Saving Customer [{}]]...", jonDoe);

			jonDoe = customerRepository.save(jonDoe);

			assertThat(jonDoe).isNotNull();
			assertThat(jonDoe.id()).isEqualTo(1L);
			assertThat(jonDoe.name()).isEqualTo("JonDoe");
			assertThat(customerRepository.count()).isEqualTo(1);

			log.info("Querying for Customer [SELECT * FROM /Customers WHERE name LIKE '%Doe']...");

			Customer queriedJonDoe = customerRepository.findByNameLike("%Doe");

			assertThat(queriedJonDoe).isEqualTo(jonDoe);

			log.info("Customer was [{}]", queriedJonDoe);
		};
	}
}
