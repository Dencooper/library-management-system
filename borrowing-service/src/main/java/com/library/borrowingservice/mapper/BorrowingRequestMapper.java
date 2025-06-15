package com.library.borrowingservice.mapper;

import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.response.BookResponse;
import com.library.commonservice.dto.response.BorrowingRequestResponse;
import com.library.commonservice.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowingRequestMapper {
        private final BooksFeignClient booksFeignClient;
        private final UsersFeignClient usersFeignClient;

        public BorrowingRequestResponse toResponse(BorrowingRequest borrowingRequest) {
                UserResponse user = usersFeignClient.getUserById(borrowingRequest.getUserId()).getBody().getData();
                List<BookResponse> books = borrowingRequest.getItems()
                                .stream()
                                .map(item -> booksFeignClient.getBook(item.getBookId()).getBody().getData())
                                .toList();

                return BorrowingRequestResponse.builder()
                                .id(borrowingRequest.getId())
                                .user(user)
                                .librarian(borrowingRequest.getLibrarianId() != null
                                                ? usersFeignClient.getUserById(borrowingRequest.getLibrarianId())
                                                                .getBody().getData()
                                                : null)
                                .requestedAt(borrowingRequest.getRequestedAt())
                                .acceptedAt(borrowingRequest.getAcceptedAt())
                                .rejectedAt(borrowingRequest.getRejectedAt())
                                .description(borrowingRequest.getDescription())
                                .status(borrowingRequest.getStatus())
                                .books(books)
                                .build();
        }
}
