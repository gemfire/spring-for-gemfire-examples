// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package dev.gemfire.actuator.event;

import dev.gemfire.actuator.model.TemperatureReading;

public class FreezingTemperatureEvent extends TemperatureEvent {

	public FreezingTemperatureEvent(Object source, TemperatureReading temperatureReading) {
		super(source, temperatureReading);
	}
}
