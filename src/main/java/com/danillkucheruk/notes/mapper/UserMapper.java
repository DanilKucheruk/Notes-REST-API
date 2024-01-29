package com.danillkucheruk.notes.mapper;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.model.User;

import java.util.stream.Collectors;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserDto> {
    private final ListMapper listMapper;

    @Override
    public UserDto map(User object) {
        return new UserDto(
            object.getId(),
            object.getUsername(),
            getListDtos(object)
        );
    }
    
    public List<ListDto> getListDtos(User object){
        return object.getLists().stream().map(listMapper::map).collect(Collectors.toList());
    }
    
}
