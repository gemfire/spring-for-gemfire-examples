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
package dev.gemfire.security.client;

import dev.gemfire.security.client.model.Customer;
import org.apache.geode.cache.client.ClientCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

/**
 * A Spring Boot, VMware GemFire {@link ClientCache} application that configures security.
*
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @see org.springframework.geode.config.annotation.EnableClusterAware
 * @since 1.4.0
 */
// tag::class[]
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = Customer.class)
public class BootGemFireSecurityClientApplication {

	private static final Logger logger = LoggerFactory.getLogger("example.app.security");

	public static void main(String[] args) {

		new SpringApplicationBuilder(BootGemFireSecurityClientApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}

	// tag::runner[]
	@Bean
	ApplicationRunner runner(@Qualifier("customersTemplate") GemfireTemplate customersTemplate) {

		return args -> {

			Customer williamEvans = new Customer(2L, "William Evans");

			customersTemplate.put(williamEvans.id(), williamEvans);

			logger.info("Successfully put [{}] in Region [{}]",
				williamEvans, customersTemplate.getRegion().getName());

			try {
				logger.info("Attempting to read from Region [{}]...", customersTemplate.getRegion().getName());
				customersTemplate.get(2L);
			}
			catch (Exception cause) {
				logger.info("Read failed because \"{}\"", cause.getCause().getCause().getMessage());
			}
		};
	}
	// end::runner[]
}
// end::class[]
