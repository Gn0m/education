package com.example.demo.jwt.config;

import com.example.demo.jwt.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter filter;
    private final AuthenticationProvider authenticationProvider;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";


    public SecurityConfig(JwtAuthenticationFilter filter,
                          @Qualifier("blockedAuthenticationProvider") AuthenticationProvider authenticationProvider) {
        this.filter = filter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/jwt/register", "/jwt/authenticate").permitAll()
                                .requestMatchers("/jwt/admin/**").hasAuthority(ADMIN)
                                .requestMatchers("/jwt/block/**").hasAuthority(ADMIN)
                                .requestMatchers(HttpMethod.DELETE).hasAuthority(ADMIN)
                                .requestMatchers("/jwt/user/**").hasAnyAuthority(USER, ADMIN)
                                .anyRequest().permitAll())
//                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
