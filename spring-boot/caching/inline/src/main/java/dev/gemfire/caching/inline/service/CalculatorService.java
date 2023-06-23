// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.service;

import dev.gemfire.caching.inline.model.Operator;
import dev.gemfire.caching.inline.model.ResultHolder;
import dev.gemfire.caching.inline.service.support.AbstractCacheableService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Spring {@link Service} class implementing mathematical operators, similar to calculator functions,
 * such as {@literal factorial} and so on.
 *
 * In addition, given that most mathematical functions yield the same result when given the same input, this service
 * also employs caching using Spring's Cache Abstraction and VMware GemFire as the caching provider.  This class provides
 * additional functionality to ascertain whether a cache hit/miss occurred.
*
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.stereotype.Service
 * @see dev.gemfire.caching.inline.model.Operator
 * @see dev.gemfire.caching.inline.model.ResultHolder
 * @see dev.gemfire.caching.inline.service.support.AbstractCacheableService
 * @since 1.1.0
 */
@Service
public class CalculatorService extends AbstractCacheableService {

	@Cacheable(value = "Factorials", keyGenerator = "resultKeyGenerator")
	public ResultHolder factorial(int number) {

		this.cacheMiss.set(true);

		Assert.isTrue(number >= 0L,
			String.format("Number [%d] must be greater than equal to 0", number));

		simulateLatency();

		if (number <= 2) {
			return ResultHolder.of(number, Operator.FACTORIAL, number == 2 ? 2 : 1);
		}

		int operand = number;
		int result = number;

		while (--number > 1) {
			result *= number;
		}

		return ResultHolder.of(operand, Operator.FACTORIAL, result);
	}

	@Cacheable(value = "SquareRoots", keyGenerator = "resultKeyGenerator")
	public ResultHolder sqrt(int number) {

		this.cacheMiss.set(true);

		Assert.isTrue(number >= 0,
			String.format("Number [%d] must be greater than equal to 0", number));

		simulateLatency();

		int result = Double.valueOf(Math.sqrt(number)).intValue();

		return ResultHolder.of(number, Operator.SQUARE_ROOT, result);
	}
}
