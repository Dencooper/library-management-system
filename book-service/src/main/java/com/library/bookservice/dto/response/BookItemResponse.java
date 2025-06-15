package com.library.bookservice.dto.response;

import com.library.commonservice.utils.constant.BookItemCondition;
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
    private boolean isAvailable;
    private String bookTitle;
    private Long bookId;
    private Double price;
    private BookItemCondition bookItemCondition;
}