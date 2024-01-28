package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;


public interface UserService extends UserDetailsService {
    UserDto create(RegistrationUserDto user);
    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    boolean delete(Long id);
}
