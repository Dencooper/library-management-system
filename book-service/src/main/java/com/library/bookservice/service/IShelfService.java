package com.library.bookservice.service;

import com.library.bookservice.dto.request.ShelfRequest;
import com.library.bookservice.dto.response.ShelfResponse;

import java.util.List;

public interface IShelfService {
    ShelfResponse createShelf(ShelfRequest request);
    ShelfResponse getShelfById(Long id);
    List<ShelfResponse> getAllShelves();
    ShelfResponse updateShelf(Long shelfId, ShelfRequest request);
    Void updateDeleteStatus(Long shelfId, boolean isDeleted);
}
