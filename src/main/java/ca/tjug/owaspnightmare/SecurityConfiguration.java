/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.tjug.owaspnightmare;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http.exceptionHandling(handling -> handling.accessDeniedHandler(new ErrorRedirectHandler()));
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/error-page").permitAll()
						.anyRequest().authenticated()
				)
			    .oauth2Login(Customizer.withDefaults());
		// @formatter:on
		return http.build();
	}

	@Slf4j
	public static class ErrorRedirectHandler implements AccessDeniedHandler {

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {

				log.info("User '" + authentication.getName() +
						 "' attempted to access the URL: " +
						 request.getRequestURI());
			}
			response.sendRedirect(request.getContextPath() + "/error-page?message=" + URLEncoder.encode(accessDeniedException.getMessage(), StandardCharsets.UTF_8));
		}
	}

}
