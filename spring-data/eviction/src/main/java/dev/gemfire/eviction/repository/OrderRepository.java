/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.eviction.repository;

import dev.gemfire.eviction.domain.Order;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.repository.CrudRepository;

@Region("Orders")
public interface OrderRepository extends CrudRepository<Order, Long> {}
