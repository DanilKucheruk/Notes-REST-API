package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.model.NoteEntity;

import io.jsonwebtoken.Claims;

public interface NoteService {
    List<NoteDto> findAll(Claims claims);
    List<NoteDto> findAllByListId(Long id);
    Optional<NoteEntity> findById(Long id, Claims claims);
    Optional<NoteDto> findNoteDtoById(Long id, Claims claims);
    boolean delete(Long id, Claims claims);
    NoteDto createNewNote(NoteCreateEditDto noteDto, Long listId);
    Optional<NoteDto> update(Long id, NoteCreateEditDto noteDto);
    
} 