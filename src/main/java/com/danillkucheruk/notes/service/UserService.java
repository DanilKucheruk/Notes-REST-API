package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.model.User;


public interface UserService extends UserDetailsService {
    User create(RegistrationUserDto user);
    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    Optional<User> findByUsername(String name);
    boolean delete(Long id);
}
