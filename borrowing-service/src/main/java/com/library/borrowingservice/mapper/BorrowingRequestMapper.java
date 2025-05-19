package com.library.borrowingservice.mapper;

import com.library.borrowingservice.dto.response.BookResponse;
import com.library.borrowingservice.dto.response.UserResponse;
import com.library.borrowingservice.dto.response.borrowingRequest.BorrowingRequestResponse;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowingRequestMapper {
    private final BooksFeignClient booksFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final AuthFeignClient authFeignClient;

    public BorrowingRequestResponse toResponse(BorrowingRequest borrowingRequest){
        UserResponse user = usersFeignClient.getUserById(borrowingRequest.getUserId()).getBody();
        List<BookResponse> books = borrowingRequest.getItems()
                        .stream()
                        .map(item -> booksFeignClient.getBook(item.getBookId()).getBody())
                        .toList();

        return BorrowingRequestResponse.builder()
                .id(borrowingRequest.getId())
                .user(user)
                .librarian(borrowingRequest.getLibrarianId() != null ? usersFeignClient.getUserById(borrowingRequest.getLibrarianId()).getBody() : null)
                .requestedAt(borrowingRequest.getRequestedAt())
                .acceptedAt(borrowingRequest.getAcceptedAt())
                .rejectedAt(borrowingRequest.getRejectedAt())
                .description(borrowingRequest.getDescription())
                .status(borrowingRequest.getStatus())
                .books(books)
                .build();
    }
}
