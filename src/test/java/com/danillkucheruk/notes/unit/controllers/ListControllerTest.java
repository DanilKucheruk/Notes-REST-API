package com.danillkucheruk.notes.unit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danillkucheruk.notes.controller.ListController;
import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.service.ListService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

@ExtendWith(MockitoExtension.class)
public class ListControllerTest {

    @Mock
    private ListService listService;

    @InjectMocks
    private ListController listController;  

    @Test
    public void getAllLists_ShouldReturnListOfListsDto() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        List<ListDto> expectedLists = new ArrayList<>();
        when(listService.findAll(anyString())).thenReturn(expectedLists);

        // when
        ResponseEntity<List<ListDto>> response = listController.getAllLists(principal);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLists, response.getBody());
        verify(listService, times(1)).findAll(anyString());
    }

    @Test
    public void getListById_ExistingId_ShouldReturnListDto() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        Long id = 1L;
        ListDto expectedList = new ListDto();
        when(listService.findById(eq(id), anyString())).thenReturn(Optional.of(expectedList));

        // when
        ResponseEntity<ListDto> response = listController.getListById(id, principal);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(listService, times(1)).findById(eq(id), anyString());
    }

    @Test
    public void getListById_NonExistingId_ShouldReturnNotFound() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        Long id = 1L;
        when(listService.findById(eq(id), anyString())).thenReturn(Optional.empty());

        // when
        ResponseEntity<ListDto> response = listController.getListById(id, principal);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(listService, times(1)).findById(eq(id), anyString());
    }

    @Test
    public void deleteListById_ExistingId_ShouldReturnOk() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        Long id = 1L;
        when(listService.delete(eq(id), anyString())).thenReturn(true);

        // when
        ResponseEntity<?> response = listController.deleteListById(id, principal);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listService, times(1)).delete(eq(id), anyString());
    }

    @Test
    public void deleteListById_NonExistingId_ShouldReturnBadRequestWithErrorMessage() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        Long id = 1L;
        when(listService.delete(eq(id), anyString())).thenReturn(false);

        // when
        ResponseEntity<?> response = listController.deleteListById(id, principal);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(listService, times(1)).delete(eq(id), anyString());
    }

    @Test
    public void createNewList_ShouldReturnCreatedListDto() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        ListCreateEditDto listDto = new ListCreateEditDto();
        ListDto expectedList = new ListDto();
        when(listService.createNewList(eq(listDto), anyString())).thenReturn(expectedList);

        // when
        ResponseEntity<ListDto> response = listController.createNewList(listDto, principal);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(listService, times(1)).createNewList(eq(listDto), anyString());
    }

    @Test
    public void updateList_ExistingId_ShouldReturnUpdatedListDto() {
        // given
        Long id = 1L;
        ListCreateEditDto listDto = new ListCreateEditDto();
        Optional<ListDto> expectedList = Optional.of(new ListDto());
        when(listService.update(eq(id), eq(listDto))).thenReturn(expectedList);

        // when
        ResponseEntity<?> response = listController.updateList(id, listDto, "jwtToken");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList.get(), response.getBody());
        verify(listService, times(1)).update(eq(id), eq(listDto));
    }

    @Test
    public void updateList_NonExistingId_ShouldReturnBadRequestWithErrorMessage() {
        // given
        Long id = 1L;
        ListCreateEditDto listDto = new ListCreateEditDto();
        when(listService.update(eq(id), eq(listDto))).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> response = listController.updateList(id, listDto, "jwtToken");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(listService, times(1)).update(eq(id), eq(listDto));
    }
}
