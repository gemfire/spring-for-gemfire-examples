// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.multisite.service;

import dev.gemfire.caching.multisite.model.Customer;
import dev.gemfire.caching.multisite.util.ThreadUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link CustomerService} is a Spring application {@link Service} component used to service interactions with
 * {@link Customer customers}.
 *
 * This service class employs the {@literal Look-Aside Caching} Pattern to lookups of {@link Customer}
 * by {@link String name} since it is not common for the {@link Customer Customer's} {@link String name}
 * to change frequently.
 *
 * Additionally, the {@link #findBy(String)} {@link CustomerService} operations simulates an expensive,
 * or resource-intensive operation by introducing a delay.
*
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.stereotype.Service
 * @see Customer
 * @since 1.3.0
 */
@Service
public class CustomerService {

	private static final long SLEEP_IN_SECONDS = 5;

	private final AtomicBoolean cacheMiss = new AtomicBoolean(false);

	private final AtomicLong customerId = new AtomicLong(0L);

	private volatile Long sleepInSeconds;

	@Cacheable("Customers")
	public Customer findBy(String name) {
		setCacheMiss();
		ThreadUtils.safeSleep(name, Duration.ofSeconds(getSleepInSeconds()));
		return new Customer(this.customerId.incrementAndGet(), name);
	}

	public boolean isCacheMiss() {
		return this.cacheMiss.compareAndSet(true, false);
	}

	protected void setCacheMiss() {
		this.cacheMiss.set(true);
	}

	public Long getSleepInSeconds() {

		Long sleepInSeconds = this.sleepInSeconds;

		return sleepInSeconds != null ? sleepInSeconds : SLEEP_IN_SECONDS;
	}

	public void setSleepInSeconds(Long seconds) {
		this.sleepInSeconds = seconds;
	}
}
