/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.execution;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;


@OnRegion(region = "Orders")
public interface OrderFunctionExecution {

	@FunctionId("sumPricesForAllProductsForOrderFnc")
	List<BigDecimal> sumPricesForAllProductsForOrder(Long orderId);
}
