/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.transactions.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
;

import dev.gemfire.transactions.domain.EmailAddress;
import dev.gemfire.transactions.domain.Customer;
import dev.gemfire.transactions.repository.CustomerRepository;
import org.apache.geode.cache.Region;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
	private final CustomerRepository customerRepository;

	@Qualifier("Customers") private Region<Long, Customer> customerRegion;

	public CustomerService(CustomerRepository customerRepository,
			@Qualifier("Customers") Region<Long, Customer> customerRegion) {
		this.customerRepository = customerRepository;
		this.customerRegion = customerRegion;
	}

	private CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public Optional<Customer> findById(long id) {
		return getCustomerRepository().findById(id);
	}

	public int numberEntriesStoredOnServer() {
		return customerRegion.keySetOnServer().size();
	}

	@Transactional
	public List<Customer> createFiveCustomers() {
		return Arrays
				.stream(new Customer[] { new Customer(1L, new EmailAddress("1@1.com"), "John", "Melloncamp"),
						new Customer(2L, new EmailAddress("2@2.com"), "Franky", "Hamilton"),
						new Customer(3L, new EmailAddress("3@3.com"), "Sebastian", "Horner"),
						new Customer(4L, new EmailAddress("4@4.com"), "Chris", "Vettel"),
						new Customer(5L, new EmailAddress("5@5.com"), "Kimi", "Rosberg") })
				.map(customerRepository::save).collect(Collectors.toList());
	}

	@Transactional
	public void updateCustomersSuccess() {
		customerRepository.save(new Customer(2L, new EmailAddress("2@2.com"), "Humpty", "Hamilton"));
	}

	@Transactional
	public void updateCustomersWithDelay(int millisDelay, Customer customer) throws InterruptedException {
		customerRepository.save(customer);
		Thread.sleep(millisDelay);
	}

	@Transactional
	public void updateCustomersFailure() {
		customerRepository.save(new Customer(2L, new EmailAddress("2@2.com"), "Numpty", "Hamilton"));
		throw new IllegalArgumentException("This is an expected exception that should fail the transactions");
	}
}
