package com.danillkucheruk.notes.dto;

import lombok.Value;

@Value
public class NoteDto {
    private Long id;

    private String title;

    private String content;
}