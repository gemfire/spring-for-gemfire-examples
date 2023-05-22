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
package dev.gemfire.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.geode.cache.client.ClientRegionShortcut;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

import dev.gemfire.configuration.model.Customer;
import dev.gemfire.configuration.repo.CustomerRepository;

/**
 * Spring Boot application implementing a Customer Service.
*
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
 * @since 1.0.0
 */
// tag::class[]
@SpringBootApplication
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableEntityDefinedRegions(basePackageClasses = Customer.class, clientRegionShortcut = ClientRegionShortcut.PROXY)
public class CustomerServiceApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(CustomerServiceApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(CustomerRepository customerRepository) {

		return args -> {
			
			Customer jonDoe = new Customer(1L, "Jon Doe");

			System.err.printf("Saving Customer [%s]%n", jonDoe);

			jonDoe = customerRepository.save(jonDoe);

			assertThat(jonDoe).isNotNull();
			assertThat(jonDoe.id()).isEqualTo(1L);
			assertThat(jonDoe.name()).isEqualTo("Jon Doe");
			assertThat(customerRepository.count()).isEqualTo(1);

			System.err.println("Querying for Customer [SELECT * FROM /Customers WHERE name LIKE '%Doe']");

			Customer queriedJonDoe = customerRepository.findByNameLike("%Doe");

			assertThat(queriedJonDoe).isEqualTo(jonDoe);

			System.err.printf("Customer was [%s]%n", queriedJonDoe);
		};
	}
}
// end::class[]
