package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.model.User;


public interface UserService extends UserDetailsService {
    //for authentication controller
    User create(RegistrationUserDto user);
    Optional<UserDto> findById(Long id);
    Optional<User> findByUsername(String name);
    List<UserDto> findAll();
    Optional<UserDto> update(Long id, RegistrationUserDto noteDto);
    boolean delete(Long id);
}
