package com.library.bookservice.controller;

import com.library.bookservice.dto.request.ShelfRequest;
import com.library.bookservice.dto.response.ShelfResponse;
import com.library.bookservice.service.IShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shelves")
@RequiredArgsConstructor
public class ShelfController {
    private final IShelfService shelfService;

    @PostMapping
    public ResponseEntity<ShelfResponse> createShelf(@RequestBody ShelfRequest request) {
        return ResponseEntity.ok(shelfService.createShelf(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfResponse> getShelfById(@PathVariable Long id){
        return ResponseEntity.ok(shelfService.getShelfById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShelfResponse>> getAllShelves() {
        return ResponseEntity.ok(shelfService.getAllShelves());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfResponse> updateShelves(@PathVariable Long id, @RequestBody ShelfRequest request) {
        return ResponseEntity.ok(shelfService.updateShelf(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateShelfStatus(@PathVariable Long id, @RequestParam boolean isDeleted) {
        shelfService.updateDeleteStatus(id, isDeleted);
        return ResponseEntity.ok().build();
    }
}
