// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.multisite;

import org.apache.geode.cache.client.ClientCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;

/**
 * The {@link MultiSiteCachingClientSite2} class is a Spring Boot, VMware GemFire {@link ClientCache}
 * Web application that can be configured to connect to 1 of many VMware GemFire clusters.
*
 * @see ClientCache
 * @see SpringApplication
 * @see SpringBootApplication
 * @see Configuration
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @since 1.3.0
 */
@SpringBootApplication
@EnableCachingDefinedRegions
@PropertySource(value="classpath:application-client-site2.properties")
public class MultiSiteCachingClientSite2 {

	public static void main(String[] args) {
		SpringApplication.run(MultiSiteCachingClientSite2.class, args);
	}
}
