package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.model.ListEntity;

import io.jsonwebtoken.Claims;

public interface ListService {
    
    List<ListDto> findAll(Claims claims);
    Optional<ListDto> findById(Long id);
    boolean delete(Long id);

    List<ListEntity> findByUserId(Long userId);
    ListDto createNewList(ListCreateEditDto listDto, String userName);
    Optional<ListDto> update(Long id, ListCreateEditDto listDto);
}