package com.danillkucheruk.notes.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.model.User;

import lombok.RequiredArgsConstructor;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class RegistrationUserMapper implements Mapper<RegistrationUserDto, User>{
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public User map(RegistrationUserDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }
    
    public void copy(RegistrationUserDto regUserDto, User user){
        user.setUsername(regUserDto.getUsername());

        Optional.ofNullable(regUserDto.getRawPassword()).filter(StringUtils::hasText)
        .map(passwordEncoder::encode).ifPresent(user::setPassword);
    }
}
