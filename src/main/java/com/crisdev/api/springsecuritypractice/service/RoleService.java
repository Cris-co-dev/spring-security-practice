package com.crisdev.api.springsecuritypractice.service;

import com.crisdev.api.springsecuritypractice.persistence.entity.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();

}
