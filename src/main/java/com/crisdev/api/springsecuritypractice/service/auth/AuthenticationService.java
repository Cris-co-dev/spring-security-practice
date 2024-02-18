package com.crisdev.api.springsecuritypractice.service.auth;

import com.crisdev.api.springsecuritypractice.dto.RegisteredUser;
import com.crisdev.api.springsecuritypractice.dto.SaveUser;
import com.crisdev.api.springsecuritypractice.dto.auth.AuthenticationRequest;
import com.crisdev.api.springsecuritypractice.dto.auth.AuthenticationResponse;
import com.crisdev.api.springsecuritypractice.persistence.entity.User;
import com.crisdev.api.springsecuritypractice.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public RegisteredUser registerOneCustomer(SaveUser newUser) {

        User user = userService.registerOneCustomer(newUser);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole().name());

        //Generar Token

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);// Busca un Provider en este caso DaoAuthenticationProvider

        User user = userService.findOneByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        AuthenticationResponse response = new AuthenticationResponse();
        response.setJwt(jwt);

        return response;
    }

    public boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
