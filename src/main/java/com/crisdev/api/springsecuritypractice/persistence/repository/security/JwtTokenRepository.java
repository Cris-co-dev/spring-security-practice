package com.crisdev.api.springsecuritypractice.persistence.repository.security;

import com.crisdev.api.springsecuritypractice.persistence.entity.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String jwt);
}
