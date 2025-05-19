package com.library.borrowingservice.dto.response.borrowing;

import com.library.borrowingservice.constant.BookItemReturnCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemResponse {
    private Long id;
    private String code;
    private String bookTitle;
    private Double price;
    private BookItemReturnCondition bookItemCondition;
    private boolean available;
}
