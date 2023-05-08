/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.execution;

import java.math.BigDecimal;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Products")
public interface ProductFunctionExecution {

	@FunctionId("sumPricesForAllProductsFnc")
	BigDecimal sumPricesForAllProducts();
}
