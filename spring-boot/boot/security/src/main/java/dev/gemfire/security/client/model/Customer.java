// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.security.client.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

/**
 * An Abstract Data Type (ADT) modeling a {@literal Customer}.
*
 * @see Id
 * @see Region
 * @since 1.0.0
 */
@Region("Customers")
public record Customer(@Id Long id, String name) {

}
