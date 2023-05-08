/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.expiration.custom.config;

import dev.gemfire.expiration.custom.domain.Product;
import org.apache.geode.cache.CustomExpiry;
import org.apache.geode.cache.ExpirationAction;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.Region;

public class CustomProductExpiry implements CustomExpiry<Long, Product> {

	private final int timeout;

	public CustomProductExpiry(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public ExpirationAttributes getExpiry(Region.Entry<Long, Product> entry) {
		if (entry.getKey() % 3 == 0) {
			return new ExpirationAttributes(timeout, ExpirationAction.INVALIDATE);
		}
		return null;
	}
}
