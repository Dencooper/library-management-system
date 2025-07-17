package com.library.borrowingservice.dto.response.borrowing;

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
    private String bookTitle;
    private Long bookId;
    private Double price;
    private BookItemCondition bookItemCondition;
    private boolean available;


}
