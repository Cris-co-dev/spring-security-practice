package com.crisdev.api.springsecuritypractice.service;

import com.crisdev.api.springsecuritypractice.dto.SaveUser;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.User;

import java.util.Optional;

public interface UserService {
    User registerOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);
}
