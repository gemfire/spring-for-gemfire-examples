// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.config;

import dev.gemfire.caching.inline.model.Operator;
import dev.gemfire.caching.inline.model.ResultHolder;
import dev.gemfire.caching.inline.repo.CalculatorRepository;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.geode.cache.InlineCachingRegionConfigurer;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Spring {@link Configuration} class used to configure VMware GemFire as a caching provider as well as configure
 * the target of JPA entity scan for application persistent entities.
 *
 * Additionally, a custom {@link KeyGenerator} bean definition is declared and used in caching to sync the keys
 * used by both the cache and the database.
*
 * @see org.springframework.boot.autoconfigure.domain.EntityScan
 * @see org.springframework.cache.interceptor.KeyGenerator
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions
 * @see org.springframework.geode.cache.InlineCachingRegionConfigurer
 * @see dev.gemfire.caching.inline.model.ResultHolder
 * @see dev.gemfire.caching.inline.repo.CalculatorRepository
 * @since 1.1.0
 */
@Configuration
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.LOCAL)
@EntityScan(basePackageClasses = ResultHolder.class)
public class CalculatorConfiguration {

	@Bean
	InlineCachingRegionConfigurer<ResultHolder, ResultHolder.ResultKey> inlineCachingForCalculatorApplicationRegionsConfigurer(
			CalculatorRepository calculatorRepository) {

		Predicate<String> regionBeanNamePredicate = regionBeanName ->
			Arrays.asList("Factorials", "SquareRoots").contains(regionBeanName);

		return new InlineCachingRegionConfigurer<>(calculatorRepository, regionBeanNamePredicate);
	}

	@Bean
	KeyGenerator resultKeyGenerator() {

		return (target, method, arguments) -> {

			int operand = Integer.parseInt(String.valueOf(arguments[0]));

			Operator operator = "sqrt".equals(method.getName())
				? Operator.SQUARE_ROOT
				: Operator.FACTORIAL;

			return ResultHolder.ResultKey.of(operand, operator);
		};
	}
}
