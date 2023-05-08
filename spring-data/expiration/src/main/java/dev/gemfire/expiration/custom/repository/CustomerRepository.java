/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.custom.repository;

import dev.gemfire.expiration.custom.domain.Customer;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.repository.CrudRepository;

@Region("Customers")
public interface CustomerRepository extends CrudRepository<Customer, Long> {}
