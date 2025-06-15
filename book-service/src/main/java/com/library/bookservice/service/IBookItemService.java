package com.library.bookservice.service;

import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.request.BookItemUpdateRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.commonservice.utils.constant.BookItemCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBookItemService {
    BookItemResponse createBookItem(BookItemCreationRequest request);

    BookItemResponse getBookItemById(Long id);

    List<BookItemResponse> getAllBookItems(Boolean isAvailable);

    List<BookItemResponse> getBookItemsByBook(Long bookId, Boolean isAvailable);

    BookItemResponse updateBookItem(Long id, BookItemUpdateRequest request);
    BookItemResponse updateAvailableBookItem(Long id, boolean isAvailable);
    BookItemResponse updateConditionBookItem(Long id, BookItemCondition condition);

    Void deleteBookItem(Long itemId, boolean isDeleted);
}
