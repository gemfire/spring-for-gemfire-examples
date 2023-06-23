// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.repo;

import dev.gemfire.caching.inline.model.Operator;
import dev.gemfire.caching.inline.model.ResultHolder;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring Data {@link CrudRepository}, a.k.a. Data Access Object (DAO) used to perform basic CRUD and simple query
 * data access operations on {@link ResultHolder} objects to/from the backend data store.
*
 * @see org.springframework.data.repository.CrudRepository
 * @see dev.gemfire.caching.inline.model.ResultHolder
 * @since 1.1.0
 */
public interface CalculatorRepository
		extends CrudRepository<ResultHolder, ResultHolder.ResultKey> {

	Optional<ResultHolder> findByOperandEqualsAndOperatorEquals(Number operand, Operator operator);

}
