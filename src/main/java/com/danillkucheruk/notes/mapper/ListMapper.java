package com.danillkucheruk.notes.mapper;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.model.ListEntity;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListMapper implements Mapper<ListEntity,ListDto>{

    private final NoteMapper noteMapper;

    @Override
    public ListDto map(ListEntity object) {
        return new ListDto(
            object.getId(), 
            object.getTitle(),
            object.getDescription(),
            object.getUser().getId(),
            object.getNotes().stream().map(noteMapper::map).collect(Collectors.toList())
            );
    }
}
