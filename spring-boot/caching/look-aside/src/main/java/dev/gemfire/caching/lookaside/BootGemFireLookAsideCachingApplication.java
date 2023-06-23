// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.lookaside;

import dev.gemfire.caching.lookaside.service.CounterService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link SpringBootApplication} to configure and bootstrap the example application using the
 * {@literal Look-Aside Caching pattern}, and specifically Spring's Cache Abstraction along with
 * VMware GemFire as the caching provider.
*
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @since 1.0.0
 */
@SpringBootApplication
public class BootGemFireLookAsideCachingApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(BootGemFireLookAsideCachingApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(CounterService counterService) {
		return args -> {
			for (int count = 1; count < 10; count++) {
				assertThat(counterService.getCount("TestCounter")).isEqualTo(count);
			}

			assertThat(counterService.getCachedCount("TestCounter")).isEqualTo(9L);
			assertThat(counterService.getCachedCount("TestCounter")).isEqualTo(9L);
			assertThat(counterService.getCachedCount("MockCounter")).isEqualTo(1L);
			assertThat(counterService.getCachedCount("MockCounter")).isEqualTo(1L);
		};
	}
}
