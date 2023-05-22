/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package dev.gemfire.intro;

import static org.assertj.core.api.Assertions.assertThat;

import dev.gemfire.intro.model.Customer;
import dev.gemfire.intro.repo.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * {@link SpringBootApplication Spring Boot application} implementing a Customer Relationship Management service (CRM).
*
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @since 1.2.0
 */
// tag::class[]
@SpringBootApplication
public class CrmApplication {

	private static final Logger log = LoggerFactory.getLogger(CrmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}

	// tag::runner[]
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
	// end::runner[]
}
// end::class[]
