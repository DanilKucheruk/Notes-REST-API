package com.danillkucheruk.notes.service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.danillkucheruk.notes.dto.AuthenticationRequest;
import com.danillkucheruk.notes.dto.AuthenticationResponse;
import com.danillkucheruk.notes.dto.RegistrationUserDto;
import com.danillkucheruk.notes.dto.UserDto;
import com.danillkucheruk.notes.exceptions.AppError;
import com.danillkucheruk.notes.model.User;
import com.danillkucheruk.notes.util.JwtTokenUtils;

import java.util.Collections;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto userdDto){
        if(userService.findByUsername(userdDto.getUsername()).isPresent()){
           return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь c указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.create(userdDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(),Collections.emptyList()));
    }
}
