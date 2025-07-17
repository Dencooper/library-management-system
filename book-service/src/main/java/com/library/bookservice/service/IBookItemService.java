package com.library.bookservice.service;

import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.commonservice.dto.request.BookItemUpdateRequest;

import java.util.List;
import java.util.Map;

public interface IBookItemService {
    BookItemResponse createBookItem(BookItemCreationRequest request);

    BookItemResponse getBookItemById(Long id);

    List<BookItemResponse> getAllBookItems(Boolean isAvailable);

    List<BookItemResponse> getBookItemsByBook(Long bookId, Boolean isAvailable);
    BookItemResponse getBookItemByCode(String code);

    BookItemResponse updateBookItem(Long id, BookItemUpdateRequest request);

    Void deleteBookItem(Long itemId, boolean isDeleted);
    Map<Long, Long> getBookIdsByItemIds(List<Long> bookItemIds);
}
