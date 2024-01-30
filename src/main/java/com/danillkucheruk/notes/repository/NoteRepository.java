package com.danillkucheruk.notes.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danillkucheruk.notes.model.NoteEntity;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity,Long>{
    List<NoteEntity> findByListId(Long id);    
}
