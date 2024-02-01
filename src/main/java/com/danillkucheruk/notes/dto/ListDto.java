package com.danillkucheruk.notes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;

    private List<NoteDto> notes;
}
