// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near;

import dev.gemfire.caching.near.model.Person;
import dev.gemfire.caching.near.service.YellowPagesService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

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
@SpringBootApplication
public class BootGemFireNearCachingClientCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootGemFireNearCachingClientCacheApplication.class, args);
	}

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
}
