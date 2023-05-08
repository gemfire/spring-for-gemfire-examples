/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.entity.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.expiration.IdleTimeoutExpiration;
import org.springframework.data.gemfire.expiration.TimeToLiveExpiration;
import org.springframework.data.gemfire.mapping.annotation.Region;


@IdleTimeoutExpiration(action = "INVALIDATE", timeout = "1")
@TimeToLiveExpiration(action = "INVALIDATE", timeout = "2")
@Region("Orders")
public record Order (@Id Long orderId, Long total, Address address) {

}
