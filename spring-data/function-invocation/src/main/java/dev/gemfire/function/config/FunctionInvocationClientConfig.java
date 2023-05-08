/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.config;

import dev.gemfire.function.domain.Customer;
import dev.gemfire.function.domain.Order;
import dev.gemfire.function.domain.Product;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;

@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
public class FunctionInvocationClientConfig {

	@Bean("Products")
	public ClientRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Product> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		regionFactoryBean.setName("Products");
		return regionFactoryBean;
	}

	@Bean("Customers")
	public ClientRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Customer> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		regionFactoryBean.setName("Customers");
		return regionFactoryBean;
	}

	@Bean("Orders")
	public ClientRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Order> regionFactoryBean = new ClientRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		regionFactoryBean.setName("Orders");
		return regionFactoryBean;
	}
}
