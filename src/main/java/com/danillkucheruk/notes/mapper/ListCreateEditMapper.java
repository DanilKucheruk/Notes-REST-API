package com.danillkucheruk.notes.mapper;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.model.ListEntity;

@Component
public class ListCreateEditMapper implements Mapper<ListCreateEditDto, ListEntity>{

    @Override
    public ListEntity map(ListCreateEditDto object) {
        ListEntity list = new ListEntity();
        copy(object,list);
        return list;

    }

    public void copy(ListCreateEditDto createListDto, ListEntity list){
        list.setDescription(createListDto.getDescription());
        list.setTitle(createListDto.getTitle());
        list.setNotes(Collections.emptyList());
    }
}
