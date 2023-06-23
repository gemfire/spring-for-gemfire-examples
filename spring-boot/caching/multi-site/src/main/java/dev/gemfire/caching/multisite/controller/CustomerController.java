// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.multisite.controller;

import dev.gemfire.caching.multisite.model.Customer;
import dev.gemfire.caching.multisite.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link CustomerController} is a Spring Web application {@link RestController} component used to
 * service user requests for {@link Customer customers} through a REST API (interface) over HTTP.
*
 * @see org.springframework.core.env.Environment
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.RestController
 * @see Customer
 * @see CustomerService
 * @since 1.3.0
 */
@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private Environment environment;

	@GetMapping("/customers/{name}")
	public CustomerHolder searchBy(@PathVariable String name) {

		return CustomerHolder.from(this.customerService.findBy(name))
			.setCacheMiss(this.customerService.isCacheMiss());
	}

	@GetMapping("/ping")
	public String pingPong() {
		return "PONG";
	}

	@GetMapping("/")
	public String home() {

		return String.format("%s is running!",
			environment.getProperty("spring.application.name", "UNKNOWN"));
	}

	public static class CustomerHolder {

		public static CustomerHolder from(Customer customer) {
			return new CustomerHolder(customer);
		}

		private boolean cacheMiss = true;

		private final Customer customer;

		protected CustomerHolder(Customer customer) {

			Assert.notNull(customer, "Customer must not be null");

			this.customer = customer;
		}

		public CustomerHolder setCacheMiss(boolean cacheMiss) {
			this.cacheMiss = cacheMiss;
			return this;

		}

		public boolean isCacheMiss() {
			return this.cacheMiss;
		}

		public Customer getCustomer() {
			return customer;
		}
	}
}
