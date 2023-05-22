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
package dev.gemfire.caching.lookaside;

import dev.gemfire.caching.lookaside.service.CounterService;
import jakarta.annotation.Resource;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for the Counter Service application.
*
 * @see org.apache.geode.cache.Region
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see org.springframework.test.context.junit4.SpringRunner
 * @see dev.gemfire.caching.lookaside.service.CounterService
 * @since 1.1.0
 */
// tag::class[]
@SpringBootTest(
	properties = { "spring.boot.data.gemfire.security.ssl.environment.post-processor.enabled=false" },
	webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class BootGeodeLookAsideCachingApplicationIntegrationTests {

	@Autowired
	private CounterService counterService;

	@Resource(name = "Counters")
	private Region<String, Long> counters;

	public void setup() {

		assertThat(this.counterService).isNotNull();
		assertThat(this.counters).isNotNull();
		assertThat(this.counters.getName()).isEqualTo("Counters");
		assertThat(this.counters).isEmpty();
	}

	public void counterServiceCachesCounts() {


	}
}
// end::class[]
