package com.library.bookservice.service.impl;

import com.library.bookservice.constant.BookItemCondition;
import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.request.BookItemUpdateRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.bookservice.mapper.BookItemMapper;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.BookItem;
import com.library.bookservice.model.Shelf;
import com.library.bookservice.repository.BookItemRepository;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.repository.ShelfRepository;
import com.library.bookservice.service.IBookItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookItemServiceImpl implements IBookItemService {
    private final BookItemRepository bookItemRepository;
    private final BookRepository bookRepository;
    private final ShelfRepository shelfRepository;
    private final BookItemMapper bookItemMapper;

    @Override
    @Transactional
    public BookItemResponse createBookItem(BookItemCreationRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + request.getBookId()));
        Shelf shelf = shelfRepository.findById(request.getShelfId())
                .orElseThrow(() -> new RuntimeException("Shelf not found with ID: " + request.getShelfId()));

        if (bookItemRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        BookItem bookItem = bookItemMapper.toEntity(request, book, shelf);
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
    public List<BookItemResponse> getAllBookItems() {
        return bookItemRepository.findAll().stream()
                .map(bookItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookItemResponse> getBookItemsByBookId(Long bookId) {
        List<BookItem> items = bookItemRepository.findByBookId(bookId);
        return items.stream()
                .map(bookItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookItemResponse updateBookItem(Long id, BookItemUpdateRequest request) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + id));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + request.getBookId()));

        Shelf shelf = shelfRepository.findById(request.getShelfId())
                .orElseThrow(() -> new RuntimeException("Shelf not found with ID: " + request.getShelfId()));

        if (bookItemRepository.existsByCode(request.getCode()) && !request.getCode().equals(bookItem.getCode())) {
            throw new RuntimeException("Code already exists");
        }

        bookItem.setCode(request.getCode());
        bookItem.setAvailable(request.getIsAvailable());
        bookItem.setBook(book);
        bookItem.setShelf(shelf);
        bookItem.setBookItemCondition(request.getBookItemCondition());

        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
    }

    @Override
    public BookItemResponse updateAvailableBookItem(Long id, boolean isAvailable) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + id));
        bookItem.setAvailable(isAvailable);
        return bookItemMapper.toResponse(bookItemRepository.save(bookItem));
    }

    @Override
    public BookItemResponse updateConditionBookItem(Long id, BookItemCondition condition) {
        BookItem bookItem = bookItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookItem not found with ID: " + id));
        bookItem.setBookItemCondition(condition);
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
}
