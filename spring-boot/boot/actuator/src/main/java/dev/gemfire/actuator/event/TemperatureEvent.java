// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.actuator.event;

import dev.gemfire.actuator.model.TemperatureReading;
import org.springframework.context.ApplicationEvent;

public class TemperatureEvent extends ApplicationEvent {

	private final TemperatureReading temperatureReading;

	public TemperatureEvent(Object source, TemperatureReading temperatureReading) {

		super(source);

		this.temperatureReading = temperatureReading;
	}

    public TemperatureReading getTemperatureReading() {
		return temperatureReading;
    }
}
