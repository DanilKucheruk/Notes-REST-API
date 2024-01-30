package com.danillkucheruk.notes.service.impl;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.mapper.ListCreateEditMapper;
import com.danillkucheruk.notes.mapper.ListMapper;
import com.danillkucheruk.notes.model.ListEntity;
import com.danillkucheruk.notes.repository.ListRepository;
import com.danillkucheruk.notes.service.ListService;
import com.danillkucheruk.notes.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {
    private final ListRepository listRepository;
    private final ListCreateEditMapper createEditListMapper;
    private final ListMapper listMapper;
    private final UserService userService;

    @Override
    public List<ListDto> findAll(Claims claims){
        return listRepository.findAll().
        stream()
        .filter(list -> list.getUser().getUsername().equals(claims.getSubject()))
        .map(listMapper::map)
        .toList();
    }

    @Override
    public Optional<ListDto> findById(Long id, Claims claims ) {
        return listRepository.
        findById(id)
        .filter(list -> list.getUser().getUsername().equals(claims.getSubject()))
        .map(listMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(Long id, Claims claims) {
        return listRepository
        .findById(id)
        .map(entity -> {
            listRepository.delete(entity);
            listRepository.flush();
            return true;
        })
        .orElse(false);
    }

    @Override
    public List<ListEntity> findByUserId(Long userId) {
        return listRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public ListDto createNewList(ListCreateEditDto listDto, String userName) {
        return Optional.of(listDto)
            .map(dto -> {
                ListEntity listEntity = createEditListMapper.map(listDto);
                listEntity.setUser(userService.findByUsername(userName).get());
                return listEntity;
            })
            .map(listRepository::save)
            .map(listMapper::map)
            .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<ListDto> update(Long id, ListCreateEditDto listDto) {
        return listRepository.findById(id)
            .map(list -> {
                ListEntity newList = new ListEntity();
                newList.setUser(list.getUser());
                newList.setId(id);
                newList.setTitle(listDto.getTitle());
                newList.setNotes(list.getNotes());
                newList.setDescription(listDto.getDescription());
                return newList;
            })
            .map(listRepository::saveAndFlush)
            .map(listMapper::map);
    }
}
