package com.danillkucheruk.notes.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.mapper.RegistrationUserMapper;
import com.danillkucheruk.notes.mapper.UserMapper;
import com.danillkucheruk.notes.model.User;
import com.danillkucheruk.notes.repository.UserRepository;
import com.danillkucheruk.notes.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegistrationUserMapper registrationUserMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(user -> new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
            ))
            .orElseThrow(() -> new UsernameNotFoundException(
                "Failed to retrieve user: " + username
            ));
    }

    @Override
    @Transactional
    public User create(RegistrationUserDto userDto) {
        return Optional.of(userDto)
                .map(dto -> {
                    return registrationUserMapper.map(dto);
                })
                .map(userRepository::save)
                .orElseThrow();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(userMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
    return userRepository.findById(id)
            .map(entity -> {
                userRepository.delete(entity);
                userRepository.flush();
                return true;
            })
            .orElse(false);
}

    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }
}
