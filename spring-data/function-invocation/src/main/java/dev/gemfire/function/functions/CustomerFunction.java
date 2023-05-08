/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.functions;

import dev.gemfire.function.domain.Customer;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerFunction implements Function {

	@Override
	public void execute(FunctionContext functionContext) {
		Object[] args = (Object[]) functionContext.getArguments();
		List<String> emailAddresses = List.of((String[])args[0]);
		List<Customer> collect = functionContext.getCache().getRegion("Customers").values()
				.parallelStream()
				.map((obj) -> (Customer) obj)
				.filter((customer) -> emailAddresses.contains(customer.getEmailAddress().getValue()))
				.collect(Collectors.toList());

		functionContext.getResultSender().lastResult(collect);
	}

	@Override
	public String getId() {
		return "listConsumersForEmailAddressesFnc";
	}

	@Override
	public boolean optimizeForWrite() {
		return true;
	}
}
