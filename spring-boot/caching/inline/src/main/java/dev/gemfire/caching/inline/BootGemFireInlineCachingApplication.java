// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline;

import dev.gemfire.caching.inline.model.Operator;
import dev.gemfire.caching.inline.model.ResultHolder;
import dev.gemfire.caching.inline.repo.CalculatorRepository;
import dev.gemfire.caching.inline.service.CalculatorService;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Boot Web application to demonstrate {@literal Inline Caching}.
*
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @see org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions
 * @since 1.1.0
 */
@SpringBootApplication
public class BootGemFireInlineCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootGemFireInlineCachingApplication.class, args);
	}
	
	@Bean
	ApplicationRunner runner(CalculatorRepository calculatorRepository, CalculatorService calculatorService,
		 @Qualifier("Factorials") Region<Object, Object> factorials, @Qualifier("SquareRoots") Region<Object, Object> squareRoots) {
		return args -> {
			assertThat(factorials).isNotNull();
			assertThat(factorials.getName()).isEqualTo("Factorials");
			assertThat(squareRoots).isNotNull();
			assertThat(squareRoots.getName()).isEqualTo("SquareRoots");

			calculatorRepository.save(ResultHolder.of(5, Operator.FACTORIAL, 120));
			calculatorRepository.save(ResultHolder.of(7, Operator.FACTORIAL, 5040));
			calculatorRepository.save(ResultHolder.of(9, Operator.FACTORIAL, 362880));
			calculatorRepository.save(ResultHolder.of(16, Operator.SQUARE_ROOT, 4));
			calculatorRepository.save(ResultHolder.of(64, Operator.SQUARE_ROOT, 8));
			calculatorRepository.save(ResultHolder.of(256, Operator.SQUARE_ROOT, 16));

			Iterable<ResultHolder> calculations = calculatorRepository.findAll();

			assertThat(calculations).isNotNull();
			assertThat(calculations).hasSize(6);
			assertThat(calculations).containsExactlyInAnyOrder(
					ResultHolder.of(5, Operator.FACTORIAL, 120),
					ResultHolder.of(7, Operator.FACTORIAL, 5040),
					ResultHolder.of(9, Operator.FACTORIAL, 362880),
					ResultHolder.of(16, Operator.SQUARE_ROOT, 4),
					ResultHolder.of(64, Operator.SQUARE_ROOT, 8),
					ResultHolder.of(256, Operator.SQUARE_ROOT, 16)
			);

			// cahce hit
			Object key = ResultHolder.ResultKey.of(7, Operator.FACTORIAL);

			assertThat(factorials).doesNotContainKey(key);
			assertThat(calculatorRepository.findByOperandEqualsAndOperatorEquals(7, Operator.FACTORIAL)
					.isPresent()).isTrue();
			assertThat(calculatorService.isCacheMiss()).isFalse();

			ResultHolder factorialOfSeven = calculatorService.factorial(7);

			assertThat(factorialOfSeven).isNotNull();
			assertThat(factorialOfSeven.getResult()).isEqualTo(5040);
			assertThat(factorials).containsKey(key);
			assertThat(calculatorService.isCacheHit()).isTrue();

			// cache miss
			key = ResultHolder.ResultKey.of(25, Operator.SQUARE_ROOT);

			assertThat(squareRoots).doesNotContainKey(key);
			assertThat(calculatorRepository.findByOperandEqualsAndOperatorEquals(25, Operator.SQUARE_ROOT)
					.isPresent()).isFalse();
			assertThat(calculatorService.isCacheMiss()).isFalse();

			ResultHolder squareRootOfTwentyFive = calculatorService.sqrt(25);

			assertThat(squareRootOfTwentyFive.getResult()).isEqualTo(5);
			assertThat(squareRoots).containsKey(key);
			assertThat(calculatorRepository.findByOperandEqualsAndOperatorEquals(25, Operator.SQUARE_ROOT)
					.isPresent()).isTrue();
			assertThat(calculatorService.isCacheMiss()).isTrue();
		};
	}
}
