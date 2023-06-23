// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.near.service;

import dev.gemfire.caching.near.model.Person;
import dev.gemfire.caching.near.service.support.AbstractCacheableService;
import dev.gemfire.caching.near.service.support.EmailGenerator;
import dev.gemfire.caching.near.service.support.PhoneNumberGenerator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
