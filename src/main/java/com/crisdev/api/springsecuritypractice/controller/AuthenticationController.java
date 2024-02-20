package com.crisdev.api.springsecuritypractice.controller;

import com.crisdev.api.springsecuritypractice.dto.auth.AuthenticationRequest;
import com.crisdev.api.springsecuritypractice.dto.auth.AuthenticationResponse;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.User;
import com.crisdev.api.springsecuritypractice.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/validate-token")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt){
        boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT_ADMIN','CUSTOMER')")
    public ResponseEntity<User> findMyProfile(){

        User user = authenticationService.findLoggedInUser();

        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthenticationResponse> autenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        AuthenticationResponse response = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(response);
    }

}
