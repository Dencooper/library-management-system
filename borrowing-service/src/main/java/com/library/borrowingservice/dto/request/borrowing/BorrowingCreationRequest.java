package com.library.borrowingservice.dto.request.borrowing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingCreationRequest {
    @NotBlank(message = "Email must be not blank")
    private String email;
    @NotEmpty(message = "At least one BookItemId must be provided")
    private List<Long> bookItemIds;
    private String description;
}
