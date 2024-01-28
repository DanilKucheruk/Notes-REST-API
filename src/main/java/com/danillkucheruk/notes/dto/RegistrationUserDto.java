package com.danillkucheruk.notes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class RegistrationUserDto {
    @NotBlank
    private String username;
    
    @NotBlank
    private String rawPassword;
}
