package br.com.migrations_flyway.config;

import br.com.migrations_flyway.Security.Jwt.JwtTokenFilter;
import br.com.migrations_flyway.Security.Jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);

            return http
                    .httpBasic(basic -> basic.disable())
                    .csrf(csrf -> csrf.disable())
                    .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(
                            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(
                            authorizeHttpRequests -> authorizeHttpRequests
                                    .requestMatchers(
                                            "/auth/signing",
                                            "/auth/refresh/**",
                                            "/swagger-ui/**",
                                            "/v3/api-docs/**"
                                    ).permitAll()
                                    .anyRequest().authenticated()
                    )
                    .cors(cors -> {})
                    .build();
        }

        /*
        "/api/**",
                "/books/**",
                "/users/**",
                "/api/file/**"
         */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
