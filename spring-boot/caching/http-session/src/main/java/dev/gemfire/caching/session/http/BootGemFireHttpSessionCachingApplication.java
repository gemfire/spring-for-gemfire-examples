// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.session.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;

/**
 * {@link SpringBootApplication} to demo HTTP Session state caching.
*
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.geode.config.annotation.EnableClusterAware
 * @since 1.1.0
 */
@SpringBootApplication
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
public class BootGemFireHttpSessionCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootGemFireHttpSessionCachingApplication.class, args);
	}
}// Copyright (c) VMware, Inc. 2023. All rights reserved. // SPDX-License-Identifier: Apache-2.0
