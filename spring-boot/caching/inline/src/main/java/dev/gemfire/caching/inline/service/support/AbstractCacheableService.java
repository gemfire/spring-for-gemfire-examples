// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.service.support;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract based class for implementing cacheable {@link Service services} along with additional functionality to
 * ascertain whether a cacheable operation led to a cache hit or a cache miss.
*
 * @since 1.1.0
 */
public abstract class AbstractCacheableService {

	protected final AtomicBoolean cacheMiss = new AtomicBoolean(false);

	public boolean isCacheHit() {
		return !isCacheMiss();
	}

	public boolean isCacheMiss() {
		return this.cacheMiss.compareAndSet(true,false);
	}

	protected long delayInMilliseconds() {
		return 3000L;
	}

	protected boolean simulateLatency() {

		try {
			Thread.sleep(delayInMilliseconds());
			return true;
		}
		catch (InterruptedException ignore) {
			Thread.currentThread().interrupt();
			return false;
		}
	}
}
