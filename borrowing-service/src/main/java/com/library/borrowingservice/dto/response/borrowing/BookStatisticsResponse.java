package com.library.borrowingservice.dto.response.borrowing;

import com.library.commonservice.dto.response.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStatisticsResponse {
    private long totalBooks;
    private long availableBooks;
    private long borrowedBooks;
    private long damagedBooks;
    private long lostBooks;
    private List<bookDTO> popularBooks;
    private double availabilityRate;
    private double utilizationRate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class bookDTO {
        private String title;
        private String author;
        private long borrowCount;
    }
}