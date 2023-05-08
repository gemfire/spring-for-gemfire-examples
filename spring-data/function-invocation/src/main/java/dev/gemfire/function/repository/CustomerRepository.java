/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.repository;

import dev.gemfire.function.domain.Customer;

import java.util.List;

import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.gemfire.repository.query.annotation.Hint;
import org.springframework.data.gemfire.repository.query.annotation.Limit;
import org.springframework.data.gemfire.repository.query.annotation.Trace;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("Customers")
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Trace
	@Limit(100)
	@Hint("emailAddressIndex")
	@Query("select * from /Customers customer where customer.emailAddress.value = $1")
	List<Customer> findByEmailAddressUsingIndex(String emailAddress);

	@Trace
	@Limit(100)
	@Query("select * from /Customers customer where customer.firstName = $1")
	List<Customer> findByFirstNameUsingIndex(String firstName);
}
