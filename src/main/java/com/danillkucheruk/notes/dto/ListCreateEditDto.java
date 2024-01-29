package com.danillkucheruk.notes.dto;

import lombok.Value;

@Value
public class ListCreateEditDto {
    private String title;

    private String description;
}
