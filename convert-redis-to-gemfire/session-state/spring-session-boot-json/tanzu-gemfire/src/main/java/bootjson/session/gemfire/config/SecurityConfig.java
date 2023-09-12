/*
Copyright 2019 - 2021 VMware, Inc.
SPDX-License-Identifier: Apache-2.0
*/

package bootjson.session.gemfire.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// @formatter:off
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(registry -> registry.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().anyRequest().authenticated())
			.formLogin(configurer-> configurer.loginPage("/login").permitAll());
		return http.build();
	}
	// @formatter:on

}
