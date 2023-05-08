/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.functions;

import dev.gemfire.function.domain.Order;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

import java.math.BigDecimal;

public class OrderFunction implements Function {

	@Override
	public void execute(FunctionContext functionContext) {
		Object[] args = (Object[]) functionContext.getArguments();
		Long orderId = (Long)args[0];
		BigDecimal total = ((Order) functionContext.getCache().getRegion("Orders").get(orderId)).calcTotal();

		functionContext.getResultSender().lastResult(total);
	}

	@Override
	public String getId() {
		return "sumPricesForAllProductsForOrderFnc";
	}
}
