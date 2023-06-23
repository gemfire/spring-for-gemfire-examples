// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.user.repository;

import dev.gemfire.user.model.User;
import org.springframework.data.gemfire.repository.GemfireRepository;

public interface UserRepository extends GemfireRepository<User, String> {
}
