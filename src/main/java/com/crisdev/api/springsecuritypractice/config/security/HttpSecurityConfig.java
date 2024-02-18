package com.crisdev.api.springsecuritypractice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//Anotaciones siempre juntas cuando se hace este tipo de config.
@Configuration
@EnableWebSecurity // Activa y configura componentes
public class HttpSecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public HttpSecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // HttpSecurity: permite personalizar como se van a gestionar y protegert las solicitudes http.
        return httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable()) //Cross-site request forgery
                .sessionManagement(sessionManagmentConfig -> sessionManagmentConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))// config para que sea sin estado
                .authenticationProvider(authenticationProvider)
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
