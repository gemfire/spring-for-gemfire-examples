// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.configuration.repo;

import dev.gemfire.configuration.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Data Access Object (DAO) used to perform basic CRUD and simple query operations on {@link Customer} objects
 * store in VMware GemFire
*
 * @see java.lang.Long
 * @see org.springframework.data.repository.CrudRepository
 * @see Customer
 * @since 1.0.0
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Customer findByNameLike(String name);

}
