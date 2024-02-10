package com.danillkucheruk.notes.integration.controller;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.integration.IntegrationTestBase;
import com.danillkucheruk.notes.integration.annotation.IT;
import com.danillkucheruk.notes.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@IT
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class NoteControllerIT extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllNotes_ReturnJsonWith2Elemtns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes").with(httpBasic("test@gmail.com", "tests"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("note1")))
                .andExpect(jsonPath("$[1].title", is("note2forList2")));
    }

    @Test
    public void testDeleteNoteById_ReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/7").with(httpBasic("test@gmail.com", "tests"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateNewNote_ReturnCreated() throws Exception {
        NoteCreateEditDto noteDto = new NoteCreateEditDto("note7", "content7");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notes")
                .with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("listId", "2")
                .content(new ObjectMapper().writeValueAsString(noteDto)))
                .andExpect(status().isCreated());

        assertTrue(noteService.findById(1L, "test@gmail.com").isPresent());
    }

    @Test
    public void testGetNoteById_ReturnNote1() throws Exception {
        mockMvc.perform(get("/api/notes/2").with(httpBasic("test@gmail.com", "tests")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("note1")));
    }

    @Test
    public void testUpdateNote_ReturnUpdatesExistingList() throws Exception {
        NoteCreateEditDto noteDto = new NoteCreateEditDto("updatedTitle", "content2");

        mockMvc.perform(put("/api/notes/2").with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isOk());

        NoteDto updatedNote = noteService.findNoteDtoById(2L, "test@gmail.com").get();

        assertEquals("updatedTitle", updatedNote.getTitle());
    }

    @Test
    public void testUpdateNoteWithInvalidId_ReturnStatus401() throws Exception {
        NoteCreateEditDto noteDto = new NoteCreateEditDto("updatedTitle", "content2");

        mockMvc.perform(put("/api/notes/1000").with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDto)))
                .andExpect(status().isBadRequest());

        assertFalse(
                noteService.findAll("test@gmail.com").stream()
                        .anyMatch(list -> list.getTitle().equals("updatedTitle")));
    }

}
