// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.lookaside.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Spring Cacheable {@link Service} class used to maintain a collection of named counters and provide counter
 * operations to increment the count, access the current, cached count and rest the count.
*
 * @see org.springframework.cache.annotation.CacheEvict
 * @see org.springframework.cache.annotation.CachePut
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.stereotype.Service
 * @since 1.0.0
 */
@Service
public class CounterService {

	private ConcurrentMap<String, AtomicLong> namedCounterMap = new ConcurrentHashMap<>();

	@Cacheable("Counters")
	public long getCachedCount(String counterName) {
		return getCount(counterName);
	}

	@CachePut("Counters")
	public long getCount(String counterName) {

		AtomicLong counter = this.namedCounterMap.get(counterName);

		if (counter == null) {

			counter = new AtomicLong(0L);

			AtomicLong existingCounter = this.namedCounterMap.putIfAbsent(counterName, counter);

			counter = existingCounter != null ? existingCounter : counter;
		}

		return counter.incrementAndGet();
	}

	@CacheEvict("Counters")
	public void resetCounter(String counterName) {
		this.namedCounterMap.remove(counterName);
	}
}
