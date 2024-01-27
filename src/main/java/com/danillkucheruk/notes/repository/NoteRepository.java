package com.danillkucheruk.notes.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.danillkucheruk.notes.model.ListEntity;
import com.danillkucheruk.notes.model.NoteEntity;
import java.util.List;


public interface NoteRepository extends JpaRepository<NoteEntity,Long>{
    List<NoteEntity> findByList(ListEntity list);    
}
