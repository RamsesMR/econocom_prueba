package apirest.prueba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Arrays;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(
					"/api/auth/login",
					"/api/auth/validate",
					"/api/auth/refresh",
					"/api/auth/sso",
					"/api/auth/sso/callback"
			).permitAll()
			.anyRequest().authenticated();

		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuracion = new CorsConfiguration();

		configuracion.setAllowedOrigins(
				Arrays.asList("http://localhost:4200")
		);

		configuracion.setAllowedMethods(
				Arrays.asList("GET", "POST")
		);

		configuracion.setAllowedHeaders(
				Arrays.asList("*")
		);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuracion);

		return source;
	}

}