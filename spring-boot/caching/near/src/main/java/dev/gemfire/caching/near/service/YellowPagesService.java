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
package dev.gemfire.caching.near.service;

import dev.gemfire.caching.near.model.Person;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import dev.gemfire.caching.near.service.support.AbstractCacheableService;
import dev.gemfire.caching.near.service.support.EmailGenerator;
import dev.gemfire.caching.near.service.support.PhoneNumberGenerator;

/**
 * Spring {@link Service} class implementing the Yellow Pages.
*
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.cache.annotation.CachePut
 * @see org.springframework.cache.annotation.CacheEvict
 * @see org.springframework.stereotype.Service
 * @see Person
 * @see AbstractCacheableService
 * @see EmailGenerator
 * @see PhoneNumberGenerator
 * @since 1.1.0
 */
// tag::class[]
@Service
public class YellowPagesService extends AbstractCacheableService {

	@Cacheable("YellowPages")
	public Person find(String name) {

		this.cacheMiss.set(true);

		Person person = new Person(name, EmailGenerator.generate(name, null), PhoneNumberGenerator.generate(null));

		simulateLatency();

		return person;
	}

	@CachePut(cacheNames = "YellowPages", key = "#person.name")
	public Person save(Person person, String email, String phoneNumber) {
		return new Person(person.name(), email, phoneNumber);
	}

	@CacheEvict(cacheNames = "YellowPages")
	public boolean evict(String name) {
		return true;
	}
}
// end::class[]
