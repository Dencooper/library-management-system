package com.library.borrowingservice.mapper;

import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.response.UserResponse;
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
                                .map((item) -> booksFeignClient.getBookItemById(item.getBookItemId()).getBody()
                                                .getData())
                                .toList();

                ResponseEntity<ApiResponse<UserResponse>> user = usersFeignClient.getUserById(borrowing.getUserId());
                ResponseEntity<ApiResponse<UserResponse>> librarian = authFeignClient.fetchUser();

                return BorrowingResponse.builder()
                                .id(borrowing.getId())
                                .user(user.getBody().getData())
                                .librarian(librarian.getBody().getData())
                                .borrowedAt(borrowing.getBorrowedAt())
                                .returnedAt(borrowing.getReturnedAt())
                                .description(borrowing.getDescription())
                                .isLate(borrowing.getIsLate())
                                .items(bookItemResponses)
                                .build();
        }
}
