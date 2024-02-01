package com.danillkucheruk.notes.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.mapper.NoteCreateEditMapper;
import com.danillkucheruk.notes.mapper.NoteMapper;
import com.danillkucheruk.notes.model.ListEntity;
import com.danillkucheruk.notes.model.NoteEntity;
import com.danillkucheruk.notes.model.User;
import com.danillkucheruk.notes.repository.ListRepository;
import com.danillkucheruk.notes.repository.NoteRepository;
import com.danillkucheruk.notes.service.impl.NoteServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImlTest{
    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @Mock 
    private NoteCreateEditMapper noteCreateEditMapper;

    @Mock
    private ListRepository listRepository;

    private static final Long NOTE_ID = 1L;
    private static final String USERNAME = "username";
    private static final User DEF_USER = new User(USERNAME, null, Collections.emptyList());
    private static final ListEntity DEF_LIST = new ListEntity(null,null, DEF_USER,Collections.emptyList());
    private static final NoteEntity DEF_NOTE = new NoteEntity(DEF_LIST,"testTitle","testContent");
    private static final NoteDto DEF_NOTE_DTO = new NoteDto(1L, "testTitle", "testContent");

    @Test
    public void findAllByListId_ShouldReturnListOfNoteDtos_WhenListIdIsValid() {
        NoteEntity note1 = DEF_NOTE;
        NoteEntity note2 = new NoteEntity();
        note2.setId(2L);
        note2.setTitle("Note 2");
        note2.setContent("Content 2");
        List<NoteEntity> notes = Arrays.asList(note1, note2);
        when(noteRepository.findByListId(1L)).thenReturn(notes);
        when(noteMapper.map(note1)).thenReturn(new NoteDto(1L, "testTitle", "testContent"));
        when(noteMapper.map(note2)).thenReturn(new NoteDto(2L, "Note 2", "Content 2"));

        List<NoteDto> result = noteService.findAllByListId(1L);

        assertEquals(2, result.size());
        assertEquals(new NoteDto(1L, "testTitle", "testContent"), result.get(0));
        assertEquals(new NoteDto(2L, "Note 2", "Content 2"), result.get(1));
    }
    
    @Test
    public void findAllByListId_ShouldReturnEmptyList_WhenListIdIsInvalid() {
        Long listId = 1L;
        when(noteRepository.findByListId(listId)).thenReturn(Collections.emptyList());

        List<NoteDto> result = noteService.findAllByListId(listId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllByUsername_ShouldReturnNoteDto_WhenNoteExists(){
        // given
        when(noteRepository.findAll()).thenReturn(List.of(DEF_NOTE));
        when(noteMapper.map(DEF_NOTE)).thenReturn(DEF_NOTE_DTO);

        // where
        List<NoteDto> result = noteService.findAll(USERNAME);

        // then
        assertEquals(1, result.size());
        assertEquals(DEF_NOTE_DTO, result.get(0));
    }


    @Test
    public void findById_ShouldReturnEmptyOptional_WhenIdIsInvalid() {
        // given
        when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.empty());

        // when
        Optional<NoteEntity> result = noteService.findById(NOTE_ID, USERNAME);

        // then
        assertTrue(result.isEmpty());
    }
   
    @Test
    public void findById_ShouldReturnEmptyOptional_WhenUsernameIsInvalid() {
        // given
        Long id = 1L;
        String username = "wrongUser";
        NoteEntity note = new NoteEntity();
        note.setId(id);
        note.setTitle("Test Note");
        note.setContent("Test Content");
        ListEntity list = DEF_LIST;
        note.setList(list);
        when(noteRepository.findById(id)).thenReturn(Optional.of(note));

        // when
        Optional<NoteEntity> result = noteService.findById(id, username);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnListOfNoteDtos_WhenUsernameIsValid() {
        // given
        NoteEntity note1 = DEF_NOTE;
        NoteEntity note2 = new NoteEntity();
        note2.setId(2L);
        note2.setTitle("Note 2");
        note2.setContent("Content 2");
        note2.setList(DEF_LIST);
        List<NoteEntity> notes = Arrays.asList(note1, note2);

        when(noteRepository.findAll()).thenReturn(notes);

        // when
        List<NoteDto> result = noteService.findAll(USERNAME);

        // then
        assertEquals(2, result.size());
    }

    @Test
    public void delete_ShouldReturnTrue_WhenNoteExistsAndBelongsToUser() {
        NoteEntity note = DEF_NOTE;
        when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.of(note));

        boolean result = noteService.delete(NOTE_ID, USERNAME);
        
        assertTrue(result);
    }

    @Test
    public void delete_ShouldReturnFalse_WhenNoteDoesNotExist() {
        when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.empty());

        boolean result = noteService.delete(NOTE_ID, USERNAME);
        
        assertFalse(result);
        verify(noteRepository, never()).delete(any());
        verify(noteRepository, never()).flush();
    }

    @Test
    public void delete_ShouldReturnFalse_WhenListExistsButDoesNotBelongToUser() {
        String username = "anotherUser";
        NoteEntity note = DEF_NOTE;
        when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.of(DEF_NOTE));

        boolean result = noteService.delete(NOTE_ID, username);
        
        assertFalse(result);
        verify(noteRepository, never()).delete(any());
        verify(noteRepository, never()).flush();
    }
    
    @Test
    public void createNewNote_ShouldReturnNoteDto_WhenNoteIsCreated() {
        // given
        NoteCreateEditDto noteDto = new NoteCreateEditDto("testTitle", "testContent");
        NoteEntity noteEntity = DEF_NOTE;
        NoteDto noteDtoExpected = DEF_NOTE_DTO;
        
        when(noteCreateEditMapper.map(noteDto)).thenReturn(noteEntity);
        when(noteRepository.save(noteEntity)).thenReturn(noteEntity);
        when(noteMapper.map(noteEntity)).thenReturn(noteDtoExpected);
        when(listRepository.findById(1l)).thenReturn(Optional.of(DEF_LIST));

        // when
        NoteDto noteDtoResult = noteService.createNewNote(noteDto, NOTE_ID);

        // then
        assertEquals(noteDtoExpected, noteDtoResult);
        verify(noteCreateEditMapper, times(1)).map(noteDto);
        verify(noteRepository, times(1)).save(noteEntity);
        verify(noteMapper, times(1)).map(noteEntity);
    }

    @Test
    public void createNewNote_ShouldThrowException_WhenListDoesNotExist() {
        NoteCreateEditDto noteDto = new NoteCreateEditDto("testTitle", "testContent");
        when(listRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,() -> noteService.createNewNote(noteDto, 1L));
    }

    @Test
    public void update_ShouldReturnUpdatedNoteDto_WhenIdAndNoteDtoAreValid() {
        // given
        Long id = 1L;

        NoteEntity noteEntity = DEF_NOTE;

        NoteDto noteDto = DEF_NOTE_DTO;

        NoteCreateEditDto noteCreateEditDto = new NoteCreateEditDto("newTitle","newContent");

        when(noteRepository.findById(id)).thenReturn(Optional.of(noteEntity));
        when(noteRepository.saveAndFlush(any(NoteEntity.class))).thenReturn(noteEntity);
        when(noteMapper.map(any(NoteEntity.class))).thenReturn(noteDto);

        // when
        Optional<NoteDto> result = noteService.update(id, noteCreateEditDto);

        // then
        assertTrue(result.isPresent());
        assertEquals(noteDto, result.get());
        verify(noteRepository, times(1)).findById(id);
        verify(noteRepository, times(1)).saveAndFlush(any(NoteEntity.class));
        verify(noteMapper, times(1)).map(any(NoteEntity.class));
    }

}   
