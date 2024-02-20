package com.crisdev.api.springsecuritypractice.service.impl;

import com.crisdev.api.springsecuritypractice.persistence.entity.security.Role;
import com.crisdev.api.springsecuritypractice.persistence.repository.security.RoleRepository;
import com.crisdev.api.springsecuritypractice.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Value("${security.default.role}")
    private  String defaultRole;

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findDefaultRole() {

        return roleRepository.findByName(defaultRole);
    }
}
