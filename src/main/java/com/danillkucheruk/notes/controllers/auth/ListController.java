package com.danillkucheruk.notes.controllers.auth;

import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.service.ListService;
import com.danillkucheruk.notes.util.JwtTokenUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListController {

    private final JwtTokenUtils jwtTokenUtils;
    @Autowired
    private ListService listService;

    @GetMapping
    public ResponseEntity<List<ListDto>> getAllLists(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        Claims claims = jwtTokenUtils.extractAllClaims(token);
        return ResponseEntity.ok(listService.findAll(claims));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDto> getListById(@PathVariable Long id) {
        return listService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListById(@PathVariable Long id) {
        if (listService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ListDto> createNewList(@RequestBody @Valid ListCreateEditDto listDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listService.createNewList(listDto, principal.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDto> updateList(@PathVariable Long id, @RequestBody ListCreateEditDto listDto, @RequestHeader("Authorization") String jwtToken) {
        Optional<ListDto> updatedList = listService.update(id, listDto);
        if (updatedList.isPresent()) {
            return ResponseEntity.ok(updatedList.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
