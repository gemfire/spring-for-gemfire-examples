// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.security.client.controller;

import dev.gemfire.security.client.BootGemFireSecurityClientApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Spring {@link RestController} used by {@link BootGemFireSecurityClientApplication}.
 *
 * @see org.springframework.web.bind.annotation.RestController
 * @since 1.4.0
 */
@RestController
public class SecurityController {

	@Autowired
	private Environment environment;

	@GetMapping("/message")
	public String getMessage() {
		return String.format("I'm using SSL with this Keystore: %s",
			this.environment.getProperty("spring.data.gemfire.security.ssl.keystore"));
	}
}
