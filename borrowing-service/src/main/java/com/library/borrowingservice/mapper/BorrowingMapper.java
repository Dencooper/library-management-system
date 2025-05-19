package com.library.borrowingservice.mapper;

import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.dto.response.UserResponse;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowingMapper {
    private final BooksFeignClient booksFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final AuthFeignClient authFeignClient;

    public BorrowingResponse toResponse(Borrowing borrowing) {
        List<BookItemResponse> bookItemResponses = borrowing.getItems()
                .stream()
                .map((item) -> booksFeignClient.getBookItemById(item.getBookItemId()).getBody())
                .toList();

        ResponseEntity<UserResponse> user = usersFeignClient.getUserById(borrowing.getUserId());
        ResponseEntity<UserResponse> librarian = authFeignClient.fetchUser();

        ResponseEntity<UserResponse> librarianResponse = borrowing.getLibrarianId() != null
                ? usersFeignClient.getUserById(borrowing.getLibrarianId()) : null;

        return BorrowingResponse.builder()
                .id(borrowing.getId())
                .user(user.getBody())
                .librarian(librarian.getBody())
                .borrowedAt(borrowing.getBorrowedAt())
                .returnedAt(borrowing.getReturnedAt())
                .penaltyDescription(borrowing.getPenaltyDescription())
                .isLate(borrowing.getIsLate())
                .items(bookItemResponses)
                .build();
    }
}
