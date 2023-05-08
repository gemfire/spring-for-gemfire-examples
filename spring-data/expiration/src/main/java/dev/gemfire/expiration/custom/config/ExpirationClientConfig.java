/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.custom.config;

import dev.gemfire.expiration.custom.domain.Customer;
import dev.gemfire.expiration.custom.domain.Product;
import org.apache.geode.cache.CustomExpiry;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableExpiration;
import org.springframework.data.gemfire.expiration.ExpirationActionType;

@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableExpiration(policies = {
		@EnableExpiration.ExpirationPolicy(timeout = 2, action = ExpirationActionType.INVALIDATE,
				regionNames = { "Customers" }, types = { EnableExpiration.ExpirationType.TIME_TO_LIVE }),
		@EnableExpiration.ExpirationPolicy(timeout = 1, action = ExpirationActionType.INVALIDATE,
				regionNames = { "Customers" }, types = { EnableExpiration.ExpirationType.IDLE_TIMEOUT }) })
public class ExpirationClientConfig {

	@Bean("IDLE")
	CustomExpiry<Long, Product> createIdleExpiration() {
		return new CustomProductExpiry(2);
	}

	@Bean("TTL")
	CustomExpiry<Long, Product> createTtlExpiration() {
		return new CustomProductExpiry(4);
	}

	@Bean("Products")
	public ClientRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemFireCache,
			@Qualifier("IDLE") CustomExpiry<Long, Product> idleExpiry,
			@Qualifier("TTL") CustomExpiry<Long, Product> ttlExpiry) {
		ClientRegionFactoryBean<Long, Product> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		regionFactoryBean.setName("Products");
		regionFactoryBean.setCustomEntryIdleTimeout(idleExpiry);
		regionFactoryBean.setCustomEntryTimeToLive(ttlExpiry);
		return regionFactoryBean;
	}

	@Bean("Customers")
	public ClientRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Customer> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		regionFactoryBean.setName("Customers");
		return regionFactoryBean;
	}
}
