package com.library.borrowingservice.dto.response.penalty;

import com.library.borrowingservice.dto.response.UserResponse;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.model.Borrowing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyResponse {
    private Long id;
    private UserResponse user;
    private double amount;
    private String description;
    private LocalDateTime penaltyAt;
    private boolean isPaid;
}
