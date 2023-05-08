/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.queries.config;

import dev.gemfire.queries.domain.Customer;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

@Configuration
@EnableContinuousQueries
@ClientCacheApplication(subscriptionEnabled = true, readyForEvents = true)
@EnableEntityDefinedRegions(clientRegionShortcut = ClientRegionShortcut.CACHING_PROXY, basePackageClasses = Customer.class)
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
public class QueryClientConfig {

}
