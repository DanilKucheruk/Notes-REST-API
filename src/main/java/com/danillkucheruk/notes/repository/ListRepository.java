package com.danillkucheruk.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danillkucheruk.notes.model.ListEntity;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<ListEntity,Long>{
    List<ListEntity> findByUserId(Long userId);
}
