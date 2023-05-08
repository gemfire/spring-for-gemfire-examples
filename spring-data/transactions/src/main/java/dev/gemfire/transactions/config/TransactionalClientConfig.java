/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.transactions.config;

import dev.gemfire.transactions.domain.Customer;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions;

@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableEntityDefinedRegions(clientRegionShortcut = ClientRegionShortcut.CACHING_PROXY, basePackageClasses = Customer.class)
@EnableGemfireCacheTransactions
public class TransactionalClientConfig {}
