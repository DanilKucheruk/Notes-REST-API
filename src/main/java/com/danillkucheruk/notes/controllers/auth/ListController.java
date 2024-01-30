package com.danillkucheruk.notes.controllers.auth;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.exceptions.AppError;
import com.danillkucheruk.notes.service.ListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListController {

    private ListService listService;

    @GetMapping
    public ResponseEntity<List<ListDto>> getAllLists(Principal principal) {
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        return ResponseEntity.ok(listService.findAll(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDto> getListById(@PathVariable Long id, Principal principal) {
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        return listService.findById(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListById(@PathVariable Long id,Principal principal) {
        String username = ((UsernamePasswordAuthenticationToken) principal).getName();
        if (listService.delete(id, username)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "List with id " + id + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ListDto> createNewList(@RequestBody @Valid ListCreateEditDto listDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listService.createNewList(listDto, principal.getName()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(@PathVariable Long id, @RequestBody ListCreateEditDto listDto, @RequestHeader("Authorization") String jwtToken) {
        Optional<ListDto> updatedList = listService.update(id, listDto);
        if (updatedList.isPresent()) {
            return ResponseEntity.ok(updatedList.get());
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "List with id " + id + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

}
