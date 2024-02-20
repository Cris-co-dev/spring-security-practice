package com.crisdev.api.springsecuritypractice.config.security;

import com.crisdev.api.springsecuritypractice.config.security.filter.JwtAuthenticationFilter;
import com.crisdev.api.springsecuritypractice.config.security.handler.CustomAccessDeniedHandler;
import com.crisdev.api.springsecuritypractice.persistence.util.RoleEnum;
import com.crisdev.api.springsecuritypractice.persistence.util.RolePermissionEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Anotaciones siempre juntas cuando se hace este tipo de config.
@Configuration
@EnableWebSecurity // Activa y configura componentes
//@EnableMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public HttpSecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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
                    buildRequestMatchersRoles(authReqConfig);
                })
                .exceptionHandling(exceptionConfig -> {
                    //Ya que los componentes AuthenticationEntryPoint y AccessDeniedHandler se registran en
                    // la cadena de filtros de seguridad (aca mismo) estos dos componentes no
                    // pueden capturar excepciones de los interceptores de anotaciones.
                    exceptionConfig.authenticationEntryPoint(this.authenticationEntryPoint);
                    //Manejo de 403
                    exceptionConfig.accessDeniedHandler(this.customAccessDeniedHandler);
                })
                .build();
    }

    private static void buildRequestMatchersAuthorities(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*
         * Autorización de endpoints de products
         */

        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAuthority(RolePermissionEnum.READ_ALL_PRODUCTS.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
                .hasAuthority(RolePermissionEnum.READ_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasAuthority(RolePermissionEnum.CREATE_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}")
                .hasAuthority(RolePermissionEnum.UPDATE_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasAuthority(RolePermissionEnum.DISABLE_ONE_PRODUCT.name());

        /*
         * Autorizacion de Categorties
         */

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAuthority(RolePermissionEnum.READ_ALL_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAuthority(RolePermissionEnum.READ_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasAuthority(RolePermissionEnum.CREATE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAuthority(RolePermissionEnum.UPDATE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasAuthority(RolePermissionEnum.DISABLE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAuthority(RolePermissionEnum.READ_MY_PROFILE.name());

        //Recursos Publicos
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

    private static void buildRequestMatchersRoles(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*
         * Autorización de endpoints de products
         */

        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasRole(RoleEnum.ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasRole(RoleEnum.ADMIN.name());

        /*
         * Autorizacion de Categorties
         */

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasRole(RoleEnum.ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasRole(RoleEnum.ADMIN.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ASSISTANT_ADMIN.name(), RoleEnum.CUSTOMER.name());

        //Recursos Publicos
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

}
