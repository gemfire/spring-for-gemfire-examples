// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.intro.config;

import dev.gemfire.intro.model.Customer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

/**
 * Spring {@link Configuration} class used to configure and initialize VMware GemFire for the CRM application
 * beyond Spring Boot {@literal auto-configuration}.
*
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
 * @see org.springframework.geode.config.annotation.EnableClusterAware
 * @since 1.2.0
 */
@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableEntityDefinedRegions(basePackageClasses = Customer.class)
public class CustomerConfiguration {

}
