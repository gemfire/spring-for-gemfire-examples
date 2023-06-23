// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.lookaside.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;

/**
 * Spring {@link Configuration} class used to configure VMware GemFire.
*
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions
 * @see org.springframework.geode.config.annotation.EnableClusterAware
 * @since 1.0.0
 */
@Configuration
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableCachingDefinedRegions
public class GemFireConfiguration { }
