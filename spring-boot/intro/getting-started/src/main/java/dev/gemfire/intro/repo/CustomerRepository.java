// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.intro.repo;

import dev.gemfire.intro.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data {@link CrudRepository} or Data Access Object (DAO) implementing basic CRUD and simple Query data access
 * operations on {@link Customer} objects.
*
 * @see org.springframework.data.repository.CrudRepository
 * @see Customer
 * @since 1.2.0
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Customer findByNameLike(String name);

}
