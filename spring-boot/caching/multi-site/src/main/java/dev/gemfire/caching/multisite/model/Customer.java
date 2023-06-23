// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.multisite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

/**
 * {@link Customer} is an Abstract Data Type (ADT) modeling a customer in a customer service application.
*
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @since 1.3.0
 */
@Region("Customers")
public record Customer(@Id Long id, String name) {

}
