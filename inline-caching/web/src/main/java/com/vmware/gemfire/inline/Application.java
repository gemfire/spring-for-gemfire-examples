/*
 * Copyright 2019 - 2021. VMware, Inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.vmware.gemfire.inline;

import com.vmware.gemfire.inline.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication
@EnableClusterDefinedRegions
@ClientCacheApplication
@EnableGemfireRepositories(basePackageClasses = ItemRepository.class)
@ComponentScan(basePackages = {"com.vmware.gemfire.inline.controller", "com.vmware.gemfire.inline.service", "com.vmware.gemfire.inline.repository"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
