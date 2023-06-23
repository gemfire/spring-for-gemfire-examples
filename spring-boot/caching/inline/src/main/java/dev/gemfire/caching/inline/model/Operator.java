// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.model;

/**
 * An {@link Class enumerated type} enumerating various mathematical operators or functions.
*
 * @since 1.1.0
 */
public enum Operator {

	FACTORIAL("factorial(%1$d) = %2$d"),
	SQUARE_ROOT("sqrt(%1$d) = %2$d");

	private final String representation;

	Operator(String definition) {
		this.representation = definition;
	}

	@Override
	public String toString() {
		return this.representation;
	}

	public String toString(Object... args) {
		return String.format(toString(), args);
	}
}
