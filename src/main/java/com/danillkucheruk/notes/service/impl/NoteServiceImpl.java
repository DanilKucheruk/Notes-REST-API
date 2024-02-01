package com.danillkucheruk.notes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.mapper.NoteCreateEditMapper;
import com.danillkucheruk.notes.mapper.NoteMapper;
import com.danillkucheruk.notes.model.NoteEntity;
import com.danillkucheruk.notes.repository.ListRepository;
import com.danillkucheruk.notes.repository.NoteRepository;
import com.danillkucheruk.notes.service.NoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    
    private final NoteRepository noteRepository;
    private final NoteCreateEditMapper noteCreateEditMapper;
    private final NoteMapper noteMapper;
    private final ListRepository listRepository;

    @Override
    public List<NoteDto> findAll(String username) {
        return noteRepository.findAll().
        stream()
        .filter(note -> note.getList().getUser().getUsername().equals(username))
        .map(noteMapper::map)
        .toList();
    }

    @Override
    public List<NoteDto> findAllByListId(Long id) {
        return noteRepository.findByListId(id).stream().map(noteMapper::map).toList();
    }

    @Override
    public Optional<NoteEntity> findById(Long id, String username) {
        return noteRepository.findById(id)
        .filter(note -> note.getList().getUser().getUsername().equals(username));
    }

    @Override
    public boolean delete(Long id,String username) {
        return noteRepository.findById(id)
        .filter(note -> note.getList().getUser().getUsername().equals(username))
        .map(entity -> {
            if (entity != null) {
                noteRepository.delete(entity);
                noteRepository.flush();
                return true;
            } else {
                return false;
            }
        }).orElse(false);
    }

    @Override
    public NoteDto createNewNote(NoteCreateEditDto noteDto, Long listId) {
        return Optional.of(noteDto)
        .map(dto -> {
            NoteEntity note = noteCreateEditMapper.map(noteDto);
            note.setList(listRepository.findById(listId).get());
            return note;
        })
        .map(noteRepository::save)
        .map(noteMapper::map)
        .orElseThrow();
    }

    @Override
    public Optional<NoteDto> update(Long id, NoteCreateEditDto noteDto) {
        return noteRepository.findById(id)
        .map(note -> {
            NoteEntity newNote = new NoteEntity();
            newNote.setId(id);
            newNote.setTitle(noteDto.getTitle());
            newNote.setContent(noteDto.getContent());
            newNote.setList(note.getList());
            return newNote;
        })
        .map(noteRepository::saveAndFlush)
        .map(noteMapper::map);
    }

    @Override
    public Optional<NoteDto> findNoteDtoById(Long id, String username) {
        return noteRepository.findById(id)
        .filter(note -> note.getList().getUser().getUsername().equals(username))
        .map(noteMapper::map);
    }
    
}
