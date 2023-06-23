// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.actuator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("TemperatureReadings")
public class TemperatureReading {

	private static final int BOILING_TEMPERATURE = 212;
	private static final int FREEZING_TEMPERATURE = 32;

	@Id
	private Long timestamp = System.currentTimeMillis();

	private final Integer temperature;

	public TemperatureReading(Integer temperature) {
		this.temperature = temperature;
	}

	@Transient
	public boolean isBoiling() {

		return temperature != null && temperature >= BOILING_TEMPERATURE;
	}

	@Transient
	public boolean isFreezing() {
		return temperature != null && temperature <= FREEZING_TEMPERATURE;
	}

	@Transient
	public boolean isNormal() {
		return !(isBoiling() || isFreezing());
	}

	@Override
	public String toString() {
		return String.format("%d °F", temperature);
	}
}
