package com.crisdev.api.springsecuritypractice.config.security.handler;

import com.crisdev.api.springsecuritypractice.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiError error = new ApiError();
        error.setBackendMessage(accessDeniedException.getLocalizedMessage());
        error.setUrl(request.getRequestURL().toString());
        error.setMethod(request.getMethod());
        error.setMessage("Lo siento, no tienes permisos suficientes para acceder a este recurso. " +
                "Por favor, ponte en contacto con el administrador del sistema para obtener ayuda.");
        error.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        String apiErrorAsJson = objectMapper.writeValueAsString(error);

        response.getWriter().write(apiErrorAsJson);
    }
}
