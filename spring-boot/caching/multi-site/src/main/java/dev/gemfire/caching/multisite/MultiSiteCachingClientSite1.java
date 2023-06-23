// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
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
