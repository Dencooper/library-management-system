package com.library.bookservice.dto.response;

import com.library.bookservice.constant.BookItemCondition;
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
    private ShelfDTO shelf;
    private boolean isAvailable;
    private String bookTitle;
    private Double price;
    private BookItemCondition bookItemCondition;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShelfDTO {
        private String code;
        private String location;
    }
}