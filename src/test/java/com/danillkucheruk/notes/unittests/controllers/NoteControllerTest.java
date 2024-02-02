package com.danillkucheruk.notes.unittests.controllers;

import com.danillkucheruk.notes.controllers.NoteController;
import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.exceptions.AppError;
import com.danillkucheruk.notes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NoteControllerTest {

    @InjectMocks
    private NoteController noteController;

    @Mock
    private NoteService noteService;

    @BeforeEach
    public void setUp() {
        noteService = mock(NoteService.class);
        noteController = new NoteController(noteService);
    }

    @Test
    public void testGetAllNotes() {
        // given
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        List<NoteDto> expectedNotes = new ArrayList<>();
        expectedNotes.add(new NoteDto(1L, "Test Note", ""));

        when(noteService.findAll(username)).thenReturn(expectedNotes);

        // when
        ResponseEntity<List<NoteDto>> response = noteController.getAllNotes(principal);

        // then
        assertEquals(expectedNotes, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(noteService, times(1)).findAll(username);
    }

    @Test
    public void testGetNoteById() {
        // given
        Long noteId = 1L;
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");
        NoteDto expectedNote = new NoteDto(1L, "Test Note", "");

        when(noteService.findNoteDtoById(noteId, username)).thenReturn(Optional.of(expectedNote));

        // when
        ResponseEntity<NoteDto> response = noteController.getNoteById(noteId, principal);

        // then
        assertEquals(expectedNote, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(noteService, times(1)).findNoteDtoById(noteId, username);
    }

    @Test
    public void testDeleteNotesById() {
        // given
        Long noteId = 1L;
        String username = "username";
        Principal principal = new UsernamePasswordAuthenticationToken(username, "password");

        when(noteService.delete(noteId, username)).thenReturn(true);

        // when
        ResponseEntity<?> response = noteController.deleteNotesById(noteId, principal);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(noteService, times(1)).delete(noteId, username);
    }

    @Test
    public void testCreateNewNote() {
        // given
        NoteCreateEditDto note = new NoteCreateEditDto("Test Note", "Description");
        Long listId = 1L;
        NoteDto expectedNote = new NoteDto(1L, "Test Note", "");

        when(noteService.createNewNote(note, listId)).thenReturn(expectedNote);

        // when
        ResponseEntity<NoteDto> response = noteController.createNewNote(note, listId);

        // then
        assertEquals(expectedNote, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(noteService, times(1)).createNewNote(note, listId);
    }

    @Test
    public void testUpdateList() {
        // given
        Long noteId = 1L;
        NoteCreateEditDto noteDto = new NoteCreateEditDto("Updated Note", "Description");
        Optional<NoteDto> updatedNote = Optional.of(new NoteDto(1L, "Test Note", ""));

        when(noteService.update(noteId, noteDto)).thenReturn(updatedNote);

        // when
        ResponseEntity<?> response = noteController.updateList(noteId, noteDto);

        // then
        assertEquals(updatedNote.get(), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(noteService, times(1)).update(noteId, noteDto);
    }

    @Test
    public void testUpdateList_NotFound() {
        // given
        Long noteId = 1L;
        NoteCreateEditDto noteDto = new NoteCreateEditDto("Updated Note", "Description");

        when(noteService.update(noteId, noteDto)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> response = noteController.updateList(noteId, noteDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Note with id 1 not found", ((AppError) response.getBody()).getMessage());

        verify(noteService, times(1)).update(noteId, noteDto);
    }
}
