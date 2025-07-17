package com.library.borrowingservice.dto.response.borrowing;

import com.library.commonservice.dto.response.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularBookResponse {
    private long borrowCount;
    private BookResponse book;
}
