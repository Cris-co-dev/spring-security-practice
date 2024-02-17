package com.crisdev.api.springsecuritypractice.service;

import com.crisdev.api.springsecuritypractice.dto.SaveUser;
import com.crisdev.api.springsecuritypractice.persistence.entity.User;

public interface UserService {
    User registerOneCustomer(SaveUser newUser);
}
