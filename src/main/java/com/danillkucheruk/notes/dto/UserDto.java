package com.danillkucheruk.notes.dto;

import java.util.List;

import lombok.Value;

@Value
public class UserDto {
    private Long id;
    private String username;
    private List<ListDto> lists;
}
