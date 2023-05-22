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
package dev.gemfire.caching.multisite;

import org.apache.geode.cache.client.ClientCache;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;

/**
 * The {@link MultiSiteCachingClientSite1} class is a Spring Boot, VMware GemFire {@link ClientCache}
 * Web application that can be configured to connect to 1 of many VMware GemFire clusters.
*
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @since 1.3.0
 */
// tag::class[]
@SpringBootApplication
@EnableCachingDefinedRegions
@PropertySource(value="classpath:application-client-site1.properties")
public class MultiSiteCachingClientSite1 {

	public static void main(String[] args) {
		SpringApplication.run(MultiSiteCachingClientSite1.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return args -> {

		};
	}
}
// end::class[]
