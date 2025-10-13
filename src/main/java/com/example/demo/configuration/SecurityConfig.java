package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailService;
	private final PasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authz -> authz
			.requestMatchers("/discard/login").permitAll()
			.anyRequest().authenticated())
			.formLogin(form -> form
			.loginPage("/discard/login")
			.loginProcessingUrl("/authentication")
			.usernameParameter("usernameInput")
			.passwordParameter("passwordInput")
			.defaultSuccessUrl("/discard")
			.failureUrl("/discard/login?error"))
			.logout(logout -> logout
			.logoutUrl("/discard/logout")
			.logoutSuccessUrl("/discard/login?logout")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			);
		return http.build();
	}

}
