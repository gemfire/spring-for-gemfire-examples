// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.actuator.repo;

import dev.gemfire.actuator.model.TemperatureReading;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureReadingRepository extends CrudRepository<TemperatureReading, Long> {

	@Query("SELECT count(*) FROM /TemperatureReadings WHERE temperature >= 212")
	Integer countBoilingTemperatureReadings();

	@Query("SELECT count(*) FROM /TemperatureReadings WHERE temperature <= 32")
	Integer countFreezingTemperatureReadings();

}
