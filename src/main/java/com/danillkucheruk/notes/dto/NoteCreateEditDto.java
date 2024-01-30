package com.danillkucheruk.notes.dto;

import lombok.Value;

@Value
public class NoteCreateEditDto {
    private String title;
    private String content;
}
