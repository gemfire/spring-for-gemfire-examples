// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.lookaside.controller;

import dev.gemfire.caching.lookaside.service.CounterService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Spring Web MVC {@link RestController} used to expose the {@link CounterService} operations via HTTP
 * in a REST-ful interface.
*
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.RestController
 * @since 1.0.0
 */
@RestController
public class CounterController {

	private static final String HEADER_ONE = "<h1>%s</h1>";

	private final CounterService counterService;

	public CounterController(CounterService counterService) {

		Assert.notNull(counterService, "CounterService is required");

		this.counterService = counterService;
	}

	@GetMapping("/")
	public String home() {
		return String.format(HEADER_ONE, "Look-Aside Caching Example");
	}

	@GetMapping("/ping")
	public String ping() {
		return String.format(HEADER_ONE, "PONG");
	}

	@GetMapping("counter/{name}")
	public String getCount(@PathVariable("name") String counterName) {
		return String.format(HEADER_ONE, this.counterService.getCount(counterName));
	}

	@GetMapping("counter/{name}/cached")
	public String getCachedCount(@PathVariable("name") String counterName) {
		return String.format(HEADER_ONE, this.counterService.getCachedCount(counterName));
	}

	@GetMapping("counter/{name}/reset")
	public String resetCounter(@PathVariable("name") String counterName) {
		this.counterService.resetCounter(counterName);
		return String.format(HEADER_ONE, "0");
	}
}
