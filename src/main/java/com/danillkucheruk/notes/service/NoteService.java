package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.model.NoteEntity;


public interface NoteService {
    List<NoteDto> findAll(String username);
    List<NoteDto> findAllByListId(Long id);
    Optional<NoteEntity> findById(Long id, String username);
    Optional<NoteDto> findNoteDtoById(Long id, String username);
    boolean delete(Long id, String username);
    NoteDto createNewNote(NoteCreateEditDto noteDto, Long listId);
    Optional<NoteDto> update(Long id, NoteCreateEditDto noteDto);
    
} 