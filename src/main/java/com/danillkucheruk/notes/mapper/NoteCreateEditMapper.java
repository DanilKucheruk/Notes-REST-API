package com.danillkucheruk.notes.mapper;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.NoteCreateEditDto;
import com.danillkucheruk.notes.model.NoteEntity;

@Component
public class NoteCreateEditMapper implements Mapper<NoteCreateEditDto,NoteEntity> {

    @Override
    public NoteEntity map(NoteCreateEditDto object) {
        NoteEntity note = new NoteEntity();
        note.setTitle(object.getTitle());
        note.setContent(object.getContent());
        return note;
    }
    
}
