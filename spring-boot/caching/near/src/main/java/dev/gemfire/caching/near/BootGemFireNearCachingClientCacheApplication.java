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
package dev.gemfire.caching.near;

import static org.assertj.core.api.Assertions.assertThat;

import dev.gemfire.caching.near.service.YellowPagesService;
import org.apache.geode.cache.Region;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.gemfire.caching.near.model.Person;

/**
 * Spring Boot application demonstrating Spring's Cache Abstraction with VMware GemFire as the caching provider
 * for {@literal Near Caching}.
*
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.client.Pool
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.context.annotation.Bean
 * @see Person
 * @since 1.1.0
 */
// tag::class[]
@SpringBootApplication
public class BootGemFireNearCachingClientCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootGemFireNearCachingClientCacheApplication.class, args);
	}

	// tag::application-runner[]
	@Bean
	public ApplicationRunner runner(YellowPagesService yellowPagesService) {

		return args -> {

			assertThat(yellowPagesService.isCacheMiss()).isFalse();

			Person jonDoe = yellowPagesService.find("Jon Doe");

			assertThat(jonDoe).isNotNull();
			assertThat(jonDoe.name()).isEqualTo("Jon Doe");
			assertThat(yellowPagesService.isCacheMiss()).isTrue();

			Person jonDoeCopy = yellowPagesService.find(jonDoe.name());

			assertThat(jonDoeCopy).isEqualTo(jonDoe);
			assertThat(yellowPagesService.isCacheMiss()).isFalse();

		};
	}
	// end::application-runner[]
}
// end::class[]
