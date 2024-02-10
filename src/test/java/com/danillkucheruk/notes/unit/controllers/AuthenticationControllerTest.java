package com.danillkucheruk.notes.unit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.danillkucheruk.notes.controller.auth.AuthenticationController;
import com.danillkucheruk.notes.dto.AuthenticationRequest;
import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authController;

    @Test
    public void createAuthToken_ShouldReturnAuthToken() {
        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        when(authService.createAuthToken(authRequest)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = authController.createAuthToken(authRequest);

        assertEquals(expectedResponse, response);
        verify(authService, times(1)).createAuthToken(authRequest);
    }

    @Test
    public void createNewUser_ShouldReturnNewUser() {
        RegistrationUserDto userDto = new RegistrationUserDto("username", "password");
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().build();
        when(authService.createNewUser(userDto)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = authController.createNewUser(userDto);

        assertEquals(expectedResponse, response);
        verify(authService, times(1)).createNewUser(userDto);
    }
}
