package com.library.bookservice.dto.request;

import com.library.bookservice.constant.BookItemCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemUpdateRequest {
    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Shelf ID is required")
    private Long shelfId;

    @NotNull(message = "Condition is required")
    private BookItemCondition bookItemCondition;

    @NotNull(message = "Availability is required")
    private Boolean isAvailable;
}