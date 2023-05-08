/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.eviction.config;

import dev.gemfire.eviction.domain.Order;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEviction;
import org.springframework.data.gemfire.eviction.EvictionActionType;
import org.springframework.data.gemfire.eviction.EvictionPolicyType;

@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableEviction(policies = @EnableEviction.EvictionPolicy(regionNames = "Orders", maximum = 10,
		action = EvictionActionType.LOCAL_DESTROY, type = EvictionPolicyType.ENTRY_COUNT))
public class EvictionClientConfig {

	@Bean("Orders")
	public ClientRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Order> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setName("Orders");
		regionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		return regionFactoryBean;
	}
}
