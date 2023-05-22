/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package dev.gemfire.actuator;

import dev.gemfire.actuator.event.BoilingTemperatureEvent;
import dev.gemfire.actuator.event.FreezingTemperatureEvent;
import dev.gemfire.actuator.event.TemperatureEvent;
import dev.gemfire.actuator.model.TemperatureReading;
import dev.gemfire.actuator.repo.TemperatureReadingRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.gemfire.PeerRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.RegionConfigurer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.geode.config.annotation.UseGroups;
import org.springframework.geode.config.annotation.UseMemberName;

import java.util.Optional;

// tag::class[]
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = TemperatureReading.class)
@EnableGemfireRepositories(basePackageClasses = TemperatureReadingRepository.class)
@UseGroups("TemperatureMonitors")
@UseMemberName("TemperatureMonitoringService")
public class BootGemFireClientApplication {

	private static final Log log = LogFactory.getLog(BootGemFireClientApplication.class);

	public static void main(String[] args) {

		new SpringApplicationBuilder(BootGemFireClientApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(TemperatureReadingRepository temperatureReadingRepository) {
		return args -> {
			temperatureReadingRepository.save(new TemperatureReading(51));
			temperatureReadingRepository.save(new TemperatureReading(30));
			temperatureReadingRepository.save(new TemperatureReading(225));
			temperatureReadingRepository.save(new TemperatureReading(0));

			log.info("Freezing: " + temperatureReadingRepository.countFreezingTemperatureReadings());
			log.info("Boiling: " + temperatureReadingRepository.countBoilingTemperatureReadings());
		};
	}

	@EventListener(classes = { BoilingTemperatureEvent.class, FreezingTemperatureEvent.class })
	public void temperatureEventHandler(TemperatureEvent temperatureEvent) {

		System.err.printf("%1$s TEMPERATURE READING [%2$s]%n",
			temperatureEvent instanceof BoilingTemperatureEvent ? "HOT" : "COLD",
				temperatureEvent.getTemperatureReading());
	}

	@Bean
	RegionConfigurer temperatureReadingsConfigurer() {

		return new RegionConfigurer() {

			@Override
			public void configure(String beanName, PeerRegionFactoryBean<?, ?> regionBean) {

				Optional.ofNullable(beanName)
						.filter("TemperatureReadings"::equals)
						.ifPresent(it -> regionBean.setStatisticsEnabled(true));
			}
		};
	}
}
// end::class[]
