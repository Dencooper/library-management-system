package com.library.bookservice.service.impl;

import com.library.bookservice.dto.request.ShelfRequest;
import com.library.bookservice.dto.response.ShelfResponse;
import com.library.bookservice.mapper.ShelfMapper;
import com.library.bookservice.model.Shelf;
import com.library.bookservice.repository.ShelfRepository;
import com.library.bookservice.service.IShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfServiceImpl implements IShelfService {
    private final ShelfRepository shelfRepository;
    private final ShelfMapper shelfMapper;

    @Override
    @Transactional
    public ShelfResponse createShelf(ShelfRequest request) {
        Shelf shelf = shelfMapper.toEntity(request);
        Shelf savedShelf = shelfRepository.save(shelf);
        return shelfMapper.toResponse(savedShelf);
    }

    @Override
    public ShelfResponse getShelfById(Long id) {
        Shelf shelf = shelfRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Shelf not found or already deleted"));
        return shelfMapper.toResponse(shelf);
    }

    @Override
    public List<ShelfResponse> getAllShelves() {
        return shelfRepository.findAllByIsDeletedFalse()
                .stream()
                .map(shelfMapper::toResponse)
                .toList();
    }

    @Override
    public ShelfResponse updateShelf(Long shelfId, ShelfRequest request) {
        Shelf shelf = shelfRepository.findByIdAndIsDeletedFalse(shelfId)
                .orElseThrow(() -> new RuntimeException("Shelf not found or already deleted"));
        shelf.setCode(request.getCode());
        shelf.setLocation(request.getLocation());
        return shelfMapper.toResponse(shelfRepository.save(shelf));
    }

    @Override
    public Void updateDeleteStatus(Long shelfId, boolean isDeleted) {
        Shelf shelf = shelfRepository.findByIdAndIsDeletedFalse(shelfId)
                .orElseThrow(() -> new RuntimeException("Shelf not found"));
        shelf.setDeleted(isDeleted);
        shelfRepository.save(shelf);
        return null;
    }
}
