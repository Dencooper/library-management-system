package com.library.bookservice.service.impl;

import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import com.library.bookservice.mapper.BookMapper;
import com.library.bookservice.model.Author;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.Category;
import com.library.bookservice.model.Shelf;
import com.library.bookservice.repository.AuthorRepository;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.repository.CategoryRepository;
import com.library.bookservice.repository.ShelfRepository;
import com.library.bookservice.service.CloudinaryService;
import com.library.bookservice.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final ShelfRepository shelfRepository;
    private final BookMapper bookMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BookResponse createBook(BookRequest request, MultipartFile imageFile) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("ISBN already exists");
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        // Check existed ISBN

        Set<Category> categories = categoryRepository.findAllByIdIn(request.getCategoryIds());

        Book book = bookMapper.toEntity(request, author, categories);

        Optional<Shelf> shelfOptional = shelfRepository.findById(request.getShelfId());
        shelfOptional.ifPresent(book::setShelf);

        // Set imageFile
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = null;
            try {
                imageUrl = cloudinaryService.uploadImage(imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            book.setImageUrl(imageUrl);
        }

        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findWithDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toResponse(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAllWithDetails()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BookResponse updateBook(Long bookId, BookRequest request) {
        Book book = bookRepository.findWithDetailsById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found or already deleted"));
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        Set<Category> categories = categoryRepository.findAllByIdIn(request.getCategoryIds());

        if (!request.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new RuntimeException("ISBN already exists");
            }
        }

        Optional<Shelf> shelfOptional = shelfRepository.findById(request.getShelfId());
        shelfOptional.ifPresent(book::setShelf);

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(author);
        book.setCategories(categories);

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toResponse(updatedBook);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BookResponse updateBookImage(Long bookId, MultipartFile imageFile) {
        Book book = bookRepository.findWithDetailsById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found or already deleted"));
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = null;
            try {
                imageUrl = cloudinaryService.uploadImage(imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            book.setImageUrl(imageUrl);
        }

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toResponse(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getBookQuantity() {
        return bookRepository.count();
    }
}
