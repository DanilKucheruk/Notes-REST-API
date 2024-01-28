package com.danillkucheruk.notes.dto;

import java.util.List;

import lombok.Value;

@Value
public class ListDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;

    private List<NoteDto> notes;
}
