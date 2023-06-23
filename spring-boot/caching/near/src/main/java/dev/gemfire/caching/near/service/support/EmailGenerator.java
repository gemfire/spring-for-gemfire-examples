// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.service.support;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates {@link String email addresses} given a person's {@link String name}.
*
 * @since 1.1.0
 */
public class EmailGenerator {

	private static final String AT_APPLE_COM = "@apple.com";
	private static final String AT_COMCAST_NET = "@comcast.net";
	private static final String AT_GMAIL_COM = "@gmail.com";
	private static final String AT_HOME_ORG = "@home.org";
	private static final String AT_MICROSOFT_COM = "@microsoft.com";
	private static final String AT_NASA_GOV = "@nasa.gov";
	private static final String AT_YAHOO_COM = "@yahoo.com";

	private static final List<String> AT_EMAIL_ADDRESSES = Arrays.asList(
		AT_APPLE_COM,
		AT_COMCAST_NET,
		AT_GMAIL_COM,
		AT_HOME_ORG,
		AT_MICROSOFT_COM,
		AT_NASA_GOV,
		AT_YAHOO_COM
	);

	private static final Random index = new Random(System.currentTimeMillis());

	public static String generate(String name, String email) {

		Assert.hasText(name, "Name is required");

		if (!StringUtils.hasText(email)) {

			name = name.toLowerCase();
			name = StringUtils.trimAllWhitespace(name);
			email = String.format("%1$s%2$s", name,
				AT_EMAIL_ADDRESSES.get(index.nextInt(AT_EMAIL_ADDRESSES.size())));
		}

		return email;
	}
}
