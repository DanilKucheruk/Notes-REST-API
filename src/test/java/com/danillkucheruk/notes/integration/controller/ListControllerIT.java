package com.danillkucheruk.notes.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.integration.IntegrationTestBase;
import com.danillkucheruk.notes.integration.annotation.IT;
import com.danillkucheruk.notes.service.ListService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@IT
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class ListControllerIT extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListService listService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllListsReturnJsonWith2Elemtns() throws Exception {
        mockMvc.perform(get("/api/lists").with(httpBasic("test@gmail.com", "tests")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("list1")))
                .andExpect(jsonPath("$[1].title", is("list2forUser1")));
    }

    @Test
    public void testGetListById_ReturnList1() throws Exception {
        mockMvc.perform(get("/api/lists/2").with(httpBasic("test@gmail.com", "tests")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("list1")));
    }

    @Test
    public void testCreateNewList_ReturnCreatesNewList() throws Exception {
        ListCreateEditDto listDto = new ListCreateEditDto();
        listDto.setTitle("newList");
        listDto.setDescription("description");

        mockMvc.perform(post("/api/lists").with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listDto)))
                .andExpect(status().isCreated());

        assertTrue(listService.findAll("test@gmail.com").size() > 0);
    }

    @Test
    public void testUpdateList_ReturnUpdatesExistingList() throws Exception {
        ListCreateEditDto listDto = new ListCreateEditDto();
        listDto.setTitle("updatedList");
        listDto.setDescription("description");
        mockMvc.perform(put("/api/lists/2").with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listDto)))
                .andExpect(status().isOk());

        ListDto updatedList = listService.findById(2L, "test@gmail.com").get();
        assertEquals("updatedList", updatedList.getTitle());
    }

    @Test
    public void testDeleteListById_ReturnStatus200() throws Exception {
        mockMvc.perform(delete("/api/lists/7").with(httpBasic("test@gmail.com", "tests")))
                .andExpect(status().isOk());

        assertFalse(listService.findById(7L, "test@gmail.com").isPresent());
    }

    @Test
    public void testUpdateListWithInvalidId() throws Exception {
        ListCreateEditDto listDto = new ListCreateEditDto();
        listDto.setTitle("updatedList");
        listDto.setDescription("description");

        mockMvc.perform(put("/api/lists/1000").with(httpBasic("test@gmail.com", "tests"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listDto)))
                .andExpect(status().isBadRequest());

        assertFalse(
                listService.findAll("test@gmail.com").stream().anyMatch(list -> list.getTitle().equals("updatedList")));
    }
}