package com.library.commonservice.dto.response;

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
public class BorrowingResponse {
    private Long id;
    private UserResponse user;
    private UserResponse librarian;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private Boolean isLate;
    private List<BookItemResponse> items;
    private String description;
}
