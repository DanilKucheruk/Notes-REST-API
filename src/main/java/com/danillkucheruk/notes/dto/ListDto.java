package com.danillkucheruk.notes.dto;

import java.util.List;

import lombok.Setter;
import lombok.Value;

@Value
@Setter
public class ListDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;

    private List<NoteDto> notes;
}
