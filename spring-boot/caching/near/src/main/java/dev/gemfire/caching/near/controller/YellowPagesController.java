// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.controller;

import dev.gemfire.caching.near.model.Person;
import dev.gemfire.caching.near.service.YellowPagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring {@link RestController} class for implementing the UI to the Yellow Pages application.
*
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.PathVariable
 * @see org.springframework.web.bind.annotation.RequestParam
 * @see org.springframework.web.bind.annotation.RestController
 * @see Person
 * @see YellowPagesService
 * @since 1.1.0
 */
@RestController
public class YellowPagesController {

	private static final String HTML =
		"<html><title>Yellow Pages</title><body bgcolor=\"#F5FC1D\" text=\"black\"><h1>%s</h1><body><html>";

	@Autowired
	private YellowPagesService yellowPages;

	@GetMapping("/")
	public String home() {
		return format("Near Caching Example");
	}

	@GetMapping("/ping")
	public String ping() {
		return format("PONG");
	}

	@GetMapping("/yellow-pages/{name}")
	public String find(@PathVariable("name") String name) {

		long t0 = System.currentTimeMillis();

		Person person = this.yellowPages.find(name);

		return format(String.format("{ person: %s, cacheMiss: %s, latency: %d ms }",
			person, this.yellowPages.isCacheMiss(), (System.currentTimeMillis() - t0)));
	}

	@GetMapping("/yellow-pages/{name}/update")
	public String update(@PathVariable("name") String name,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "phoneNumber", required = false) String phoneNumber) {

		Person person = this.yellowPages.save(this.yellowPages.find(name), email, phoneNumber);

		return format(String.format("{ person: %s }", person));
	}

	@GetMapping("/yellow-pages/{name}/evict")
	public String evict(@PathVariable("name") String name) {

		this.yellowPages.evict(name);

		return format(String.format("Evicted %s", name));
	}

	private String format(String value) {
		return String.format(HTML, value);
	}
}
