package com.library.borrowingservice.dto.request.borrowing;

import com.library.commonservice.utils.constant.BookItemCondition;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnBookRequest {
    @NotNull
    private Long borrowingId;

    @NotEmpty(message = "At least one BookItemId must be provided")
    private List<ReturnBookItemDTO> items = new ArrayList<>();

    private String description;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnBookItemDTO {
        private Long bookItemId;

        @Enumerated(EnumType.STRING)
        private BookItemCondition bookItemCondition;
    }
}