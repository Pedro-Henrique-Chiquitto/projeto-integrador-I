package com.esposto.UserRegister.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/",
								"/index.html",
								"/contact",
								"/store/**",
								"/register",
								"/login",
								"/logout",
								"/styles/**",
								"/images/**",
								"/js/**",
								"/webjars/**",
								"/favicon.ico"
						).permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/")  // Página de login é a raiz (index.html)
						.loginProcessingUrl("/login")  // URL para processar o login
						.defaultSuccessUrl("/agenda", true)  // Redireciona para /home após login
						.failureUrl("/?error=true")  // Redireciona para index com erro
						.permitAll()
				)
				.logout(config -> config
						.logoutUrl("/logout")
						.logoutSuccessUrl("/?logout=true")
						.permitAll()
				)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}