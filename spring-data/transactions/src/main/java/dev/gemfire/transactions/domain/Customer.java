/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.transactions.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;


@Region(name = "Customers")
public record Customer (@Id Long id, EmailAddress emailAddress, String firstName, String lastName) {

}