package com.library.borrowingservice.dto.response.borrowing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingStatisticsResponse {
    private long totalBorrowings;
    private long onTimeReturns;
    private long lateReturns;
    private long activeBorrowing;
    private long overdueBorrowing;
    private double averageBorrowDuration;
}
