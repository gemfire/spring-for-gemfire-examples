// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.service.support;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base class for implementing cacheable {@link Service services} along with additional functionality to
 * ascertain whether a cacheable operation led to a cache hit or a cache miss.
*
 * @since 1.1.0
 */
public abstract class AbstractCacheableService {

	protected static final int BOUNDED_MULTIPLIER = 3;

	protected static final long BASE_MILLISECONDS = 2000L;
	protected static final long ONE_SECOND_IN_MILLISECONDS = 1000L;

	protected final AtomicBoolean cacheMiss = new AtomicBoolean(false);

	protected final Random multiplier = new Random(System.currentTimeMillis());

	public boolean isCacheHit() {
		return !isCacheMiss();
	}

	public boolean isCacheMiss() {
		return this.cacheMiss.compareAndSet(true,false);
	}

	protected long delayInMilliseconds() {
		return BASE_MILLISECONDS + (ONE_SECOND_IN_MILLISECONDS * this.multiplier.nextInt(BOUNDED_MULTIPLIER));
	}

	protected boolean simulateLatency() {
		return simulateLatency(delayInMilliseconds());
	}

	protected boolean simulateLatency(long milliseconds) {

		try {
			Thread.sleep(milliseconds);
			return true;
		}
		catch (InterruptedException ignore) {
			Thread.currentThread().interrupt();
			return false;
		}
	}
}
