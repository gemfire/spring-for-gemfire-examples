// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.config;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.InterestResultPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.Interest;
import org.springframework.data.gemfire.client.RegexInterest;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.RegionConfigurer;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.geode.cache.AbstractCommonEventProcessingCacheListener;

/**
 * Spring {@link Configuration} class to configure VMware GemFire client {@link Region Regions}
 * with interest registration on all keys.
*
 * @see org.apache.geode.cache.Region
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.client.ClientRegionFactoryBean
 * @see org.springframework.data.gemfire.client.Interest
 * @see org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions
 * @see org.springframework.data.gemfire.config.annotation.RegionConfigurer
 * @since 1.1.0
 */
@Configuration
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.CACHING_PROXY)
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
public class GemFireConfiguration {


	@Bean
	RegionConfigurer interestRegisteringRegionConfigurer() {

		return new RegionConfigurer() {

			@Override
			@SuppressWarnings("unchecked")
			public void configure(String beanName, ClientRegionFactoryBean<?, ?> clientRegion) {

				Interest interest = new RegexInterest(".*", InterestResultPolicy.NONE,
					false, true);

				clientRegion.setInterests(ArrayUtils.asArray(interest));
			}
		};
	}

	@Bean
	RegionConfigurer subscriptionCacheListenerRegionConfigurer() {

		return new RegionConfigurer() {

			@Override
			@SuppressWarnings("unchecked")
			public void configure(String beanName, ClientRegionFactoryBean<?, ?> clientRegion) {

				CacheListener subscriptionCacheListener =
						new AbstractCommonEventProcessingCacheListener() {

					@Override
					protected void processEntryEvent(EntryEvent event, EntryEventType eventType) {

						if (event.isOriginRemote()) {
							System.err.printf("[%1$s] EntryEvent for [%2$s] with value [%3$s]%n",
								event.getKey(), event.getOperation(), event.getNewValue());
						}
					}
				};

				clientRegion.setCacheListeners(ArrayUtils.asArray(subscriptionCacheListener));
			}
		};
	}
}
