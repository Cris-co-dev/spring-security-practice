package com.crisdev.api.springsecuritypractice.exception;

import com.crisdev.api.springsecuritypractice.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Generic messages
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception e, HttpServletRequest httpServletRequest){

        ApiError error = new ApiError();
        error.setBackendMessage(e.getLocalizedMessage());
        error.setUrl(httpServletRequest.getRequestURL().toString());
        error.setMethod(httpServletRequest.getMethod());
        error.setMessage("Error interno en el servidor, vuelva a intentarlo");
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                    HttpServletRequest httpServletRequest){
        ApiError error = new ApiError();
        error.setBackendMessage(e.getLocalizedMessage());
        error.setUrl(httpServletRequest.getRequestURL().toString());
        error.setMethod(httpServletRequest.getMethod());
        error.setMessage("Error en la petición enviada");
        error.setTimestamp(LocalDateTime.now());

        System.out.println(e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }




}
