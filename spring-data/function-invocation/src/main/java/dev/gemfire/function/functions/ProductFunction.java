/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.functions;

import dev.gemfire.function.domain.Product;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

import java.math.BigDecimal;

public class ProductFunction implements Function {

	@Override
	public void execute(FunctionContext functionContext) {
		BigDecimal productTotal = functionContext.getCache().getRegion("Products").values()
				.parallelStream()
				.map((obj) -> ((Product) obj).getPrice())
				.reduce(BigDecimal::add).get();

		functionContext.getResultSender().lastResult(productTotal);
	}

	@Override
	public String getId() {
		return "sumPricesForAllProductsFnc";
	}
}
