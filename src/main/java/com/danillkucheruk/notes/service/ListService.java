package com.danillkucheruk.notes.service;

import java.util.List;
import java.util.Optional;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.model.ListEntity;

import io.jsonwebtoken.Claims;

public interface ListService {
    
    //We pass Claims to the methods so that users can view and delete only their lists

    //find all lists for auth user
    List<ListDto> findAll(Claims claims);
    Optional<ListDto> findById(Long id, Claims claims);
    boolean delete(Long id, Claims claims);

    List<ListEntity> findByUserId(Long userId);
    ListDto createNewList(ListCreateEditDto listDto, String userName);
    Optional<ListDto> update(Long id, ListCreateEditDto listDto);
}