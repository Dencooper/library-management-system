package com.library.bookservice.service.impl;

import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.bookservice.mapper.BookItemMapper;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.BookItem;
import com.library.bookservice.repository.BookItemRepository;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.service.IBookItemService;
import com.library.commonservice.dto.request.BookItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookItemServiceImpl implements IBookItemService {
    private final BookItemRepository bookItemRepository;
    private final BookRepository bookRepository;
    private final BookItemMapper bookItemMapper;

    @Override
    @Transactional
    public BookItemResponse createBookItem(BookItemCreationRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + request.getBookId()));

        if (bookItemRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        BookItem bookItem = bookItemMapper.toEntity(request, book);
        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
    }

    @Override
    @Transactional(readOnly = true)
    public BookItemResponse getBookItemById(Long id) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + id));
        return bookItemMapper.toResponse(bookItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookItemResponse> getAllBookItems(Boolean isAvailable) {
        if(isAvailable == null) {
            return bookItemRepository.findByIsDeletedFalse().stream()
                    .map(bookItemMapper::toResponse)
                    .toList();
        } else {
            return bookItemRepository.findByIsDeletedFalseAndIsAvailable(isAvailable).stream()
                    .map(bookItemMapper::toResponse)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookItemResponse> getBookItemsByBook(Long bookId, Boolean isAvailable) {
        List<BookItem> items = isAvailable != null ?
                bookItemRepository.findByBookIdAndIsAvailable(bookId, isAvailable) :
                bookItemRepository.findByBookId(bookId);
        return items.stream()
                .map(bookItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookItemResponse getBookItemByCode(String code) {
        BookItem bookItem = bookItemRepository.findByCodeAndIsAvailableTrue(code)
                .orElseThrow(() -> new RuntimeException("BookItem not found with code: " + code));
        return bookItemMapper.toResponse(bookItem);
    }

    @Override
    @Transactional
    public BookItemResponse updateBookItem(Long id, BookItemUpdateRequest request) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + id));

        if (bookItemRepository.existsByCode(request.getCode()) && !request.getCode().equals(bookItem.getCode())) {
            throw new RuntimeException("Code already exists");
        }

        if(request.getCode() != null) {
            bookItem.setCode(request.getCode());
        }
        bookItem.setAvailable(request.getIsAvailable());
        bookItem.setBookItemCondition(request.getBookItemCondition());

        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
    }

    @Override
    public Void deleteBookItem(Long itemId, boolean isDeleted) {
        BookItem bookItem = bookItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + itemId));
        bookItem.setDeleted(isDeleted);
        bookItemRepository.save(bookItem);
        return null;
    }

    @Override
    public Map<Long, Long> getBookIdsByItemIds(List<Long> bookItemIds) {
        return bookItemRepository.findAllById(bookItemIds).stream()
                .collect(Collectors.toMap(
                        BookItem::getId,
                        item -> item.getBook().getId()
                ));
    }
}
