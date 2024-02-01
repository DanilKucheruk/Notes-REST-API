package com.danillkucheruk.notes.dto;

import lombok.Setter;
import lombok.Value;

@Value
@Setter
public class NoteCreateEditDto {
    private String title;
    private String content;
}
