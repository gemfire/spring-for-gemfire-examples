// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.service.support;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates random phone number using a few well-defined area codes.
*
 * @since 1.1.0
 */
public class PhoneNumberGenerator {

	private static int CALIFORNIA_AREA_CODE = 707;
	private static int IOWA_AREA_CODE = 319;
	private static int MONTANA_AREA_CODE = 406;
	private static int NEW_YORK_AREA_CODE = 914;
	private static int OREGON_AREA_CODE = 503;
	private static int WASHINGTON_AREA_CODE = 206;
	private static int WISCONSIN_AREA_CODE = 608;

	private static final List<Integer> PHONE_NUMBER_AREA_CODES = Arrays.asList(
		CALIFORNIA_AREA_CODE,
		IOWA_AREA_CODE,
		MONTANA_AREA_CODE,
		NEW_YORK_AREA_CODE,
		OREGON_AREA_CODE,
		WASHINGTON_AREA_CODE,
		WISCONSIN_AREA_CODE
	);

	private static final Random index = new Random(System.currentTimeMillis());

	public static String generate(String phoneNumber) {

		if (!StringUtils.hasText(phoneNumber)) {

			phoneNumber = String.valueOf(PHONE_NUMBER_AREA_CODES.get(index.nextInt(PHONE_NUMBER_AREA_CODES.size())))
				.concat("-")
				.concat(String.valueOf(index.nextInt(9)))
				.concat(String.valueOf(index.nextInt(9)))
				.concat(String.valueOf(index.nextInt(9)))
				.concat("-")
				.concat(String.valueOf(index.nextInt(9)))
				.concat(String.valueOf(index.nextInt(9)))
				.concat(String.valueOf(index.nextInt(9)))
				.concat(String.valueOf(index.nextInt(9)));
		}

		return phoneNumber;
	}
}
