package com.library.borrowingservice.dto.response.borrowingRequest;

import com.library.borrowingservice.constant.BorrowingRequestStatus;
import com.library.borrowingservice.dto.response.BookResponse;
import com.library.borrowingservice.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRequestResponse {
    private Long id;
    private UserResponse user;
    private UserResponse librarian;
    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;
    private BorrowingRequestStatus status;
    private String description;
    private List<BookResponse> books;
}