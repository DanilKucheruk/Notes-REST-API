package com.danillkucheruk.notes.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.danillkucheruk.notes.dto.AuthenticationRequest;
import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.mapper.UserMapper;
import com.danillkucheruk.notes.service.AuthenticationService;
import com.danillkucheruk.notes.service.UserService;
import com.danillkucheruk.notes.util.JwtTokenUtils;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;



    @Test
    public void createAuthToken_ShouldReturnToken_WhenCredentialsAreValid() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("validUsername", "validPassword");
        UserDetails userDetails = new User("validUsername", "validPassword", Collections.emptyList());
        String token = "generatedToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.loadUserByUsername("validUsername")).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);

        // when
        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createAuthToken_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("invalidUsername", "invalidPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        // when
        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void createNewUser_ShouldReturnBadRequest_WhenUserAlreadyExists() {
        // given
        RegistrationUserDto userDto = new RegistrationUserDto("existingUser", "password");

        when(userService.findByUsername("existingUser")).thenReturn(Optional.of(new com.danillkucheruk.notes.model.User()));

        // when
        ResponseEntity<?> response = authService.createNewUser(userDto);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


@Test
public void createNewUser_ShouldReturnUserDto_WhenUserIsCreated() {
    // given
    RegistrationUserDto userDto = new RegistrationUserDto("newUser", "password");
    com.danillkucheruk.notes.model.User user = new com.danillkucheruk.notes.model.User();
    user.setId(1L);
    user.setUsername("newUser");

    when(userService.create(userDto)).thenReturn(user);

    // when
    ResponseEntity<?> response = authService.createNewUser(userDto);

    // then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
}


}
