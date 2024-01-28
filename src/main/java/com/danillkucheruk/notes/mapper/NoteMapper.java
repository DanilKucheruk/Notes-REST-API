package com.danillkucheruk.notes.mapper;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.NoteDto;
import com.danillkucheruk.notes.model.NoteEntity;

@Component
public class NoteMapper implements Mapper<NoteEntity,NoteDto>{
    @Override
    public NoteDto map(NoteEntity object) {
        return new NoteDto(object.getId(), object.getTitle(), object.getContent());
    }
}
