package com.library.bookservice.mapper;

import com.library.bookservice.dto.request.ShelfRequest;
import com.library.bookservice.dto.response.ShelfResponse;
import com.library.bookservice.model.Shelf;
import org.springframework.stereotype.Component;

@Component
public class ShelfMapper {
    public Shelf toEntity(ShelfRequest request) {
        Shelf shelf = new Shelf();
        shelf.setCode(request.getCode());
        shelf.setLocation(request.getLocation());
        return shelf;
    }

    public ShelfResponse toResponse(Shelf shelf) {
        ShelfResponse response = new ShelfResponse();
        response.setId(shelf.getId());
        response.setCode(shelf.getCode());
        response.setLocation(shelf.getLocation());
        response.setCreatedAt(shelf.getCreatedAt());
        response.setUpdatedAt(shelf.getUpdatedAt());
        return response;
    }
}
