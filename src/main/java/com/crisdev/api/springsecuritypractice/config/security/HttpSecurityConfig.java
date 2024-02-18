package com.crisdev.api.springsecuritypractice.config.security;

import com.crisdev.api.springsecuritypractice.config.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Anotaciones siempre juntas cuando se hace este tipo de config.
@Configuration
@EnableWebSecurity // Activa y configura componentes
public class HttpSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public HttpSecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // HttpSecurity: permite personalizar como se van a gestionar y protegert las solicitudes http.
        return httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable()) //Cross-site request forgery
                .sessionManagement(sessionManagmentConfig -> sessionManagmentConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))// config para que sea sin estado
                .authenticationProvider(authenticationProvider)
                // A los filtros personalizados se debe poner un orden entre 0 y +2000
                // En este caso UsernamePasswordAuthenticationFilter tiene un peso de 1900.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authReqConfig -> {
                    //Recursos Publicos
                    authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

                    // Recursos asegurados
                    authReqConfig.anyRequest().authenticated();
                }).build();
    }


}
