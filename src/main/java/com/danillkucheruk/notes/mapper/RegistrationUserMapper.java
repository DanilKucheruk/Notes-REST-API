package com.danillkucheruk.notes.mapper;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationUserMapper implements Mapper<RegistrationUserDto, User>{

    @Override
    public User map(RegistrationUserDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }
    
    public void copy(RegistrationUserDto regUserDto, User user){
        user.setUsername(regUserDto.getUsername());
    }
}
