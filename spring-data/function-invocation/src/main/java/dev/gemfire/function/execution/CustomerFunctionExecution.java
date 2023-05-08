/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.execution;

import dev.gemfire.function.domain.Customer;

import java.util.List;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Customers")
public interface CustomerFunctionExecution {

	@FunctionId("listConsumersForEmailAddressesFnc")
	List<List<Customer>> listAllCustomersForEmailAddress(String... emailAddresses);
}
