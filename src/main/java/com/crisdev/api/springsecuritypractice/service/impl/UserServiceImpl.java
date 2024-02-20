package com.crisdev.api.springsecuritypractice.service.impl;

import com.crisdev.api.springsecuritypractice.dto.SaveUser;
import com.crisdev.api.springsecuritypractice.exception.InvalidPasswordException;
import com.crisdev.api.springsecuritypractice.exception.ObjectNotFoundException;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.Role;
import com.crisdev.api.springsecuritypractice.persistence.entity.security.User;
import com.crisdev.api.springsecuritypractice.persistence.repository.security.UserRepository;
import com.crisdev.api.springsecuritypractice.service.RoleService;
import com.crisdev.api.springsecuritypractice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerOneCustomer(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Role defaulRole = roleService.findDefaultRole()
                .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default role"));

        user.setRole(defaulRole);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(SaveUser newUser) {

        if (!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

    }
}
