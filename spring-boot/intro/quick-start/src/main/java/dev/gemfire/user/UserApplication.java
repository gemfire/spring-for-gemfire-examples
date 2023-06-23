// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.user;

import dev.gemfire.user.model.User;
import dev.gemfire.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.geode.config.annotation.EnableClusterAware;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example {@link SpringBootApplication} using VMware GemFire to manage Users.
*
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.repository.CrudRepository
 * @see org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @see org.springframework.data.gemfire.repository.GemfireRepository
 * @see org.springframework.geode.config.annotation.EnableClusterAware
 * @since 1.0.0
 */
@SpringBootApplication
@EnableClusterAware
@EnableEntityDefinedRegions(basePackageClasses = User.class)
public class UserApplication {

	private static final Logger log = LoggerFactory.getLogger(UserApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
		ApplicationRunner runner(UserRepository userRepository) {

		return args -> {

			long count = userRepository.count();

			assertThat(count).isZero();

			log.info("Number of Users [{}]", count);

			User jonDoe = new User("jonDoe");

			log.info("Created User [{}]", jonDoe);

			userRepository.save(jonDoe);

			log.info("Saved User [{}]", jonDoe);

			count = userRepository.count();

			assertThat(count).isOne();

			log.info("Number of Users [{}]", count);

			User jonDoeFoundById = userRepository.findById(jonDoe.name()).orElse(null);

			assertThat(jonDoeFoundById).isEqualTo(jonDoe);

			log.info("Found User by ID (name) [{}]", jonDoeFoundById);
		};
	}
}

