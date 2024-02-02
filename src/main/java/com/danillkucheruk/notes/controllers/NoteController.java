package com.danillkucheruk.notes.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.exceptions.AppError;
import com.danillkucheruk.notes.service.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes(Principal principal){
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        return ResponseEntity.ok(noteService.findAll(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id, Principal principal){
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        return noteService.findNoteDtoById(id, username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotesById(@PathVariable Long id, Principal principal){
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        if(noteService.delete(id, username)){
            return ResponseEntity.ok().build();
        }else{
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Note with id " + id + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNewNote(@RequestBody @Valid NoteCreateEditDto note, @RequestParam Long listId){
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNewNote(note, listId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(@PathVariable Long id,@RequestBody NoteCreateEditDto noteDto){
        Optional<NoteDto> updatedNote = noteService.update(id, noteDto);
        if(updatedNote.isPresent()){
            return ResponseEntity.ok(updatedNote.get());
        }else{
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Note with id " + id + " not found"), HttpStatus.BAD_REQUEST);
        }

    }
}

