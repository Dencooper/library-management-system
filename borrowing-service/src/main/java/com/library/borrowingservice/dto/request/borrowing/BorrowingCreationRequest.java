package com.library.borrowingservice.dto.request.borrowing;

import com.library.borrowingservice.model.BorrowingRequest;
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
    @NotNull(message = "BorrowingRequest is required")
    private Long borrowingRequestId;

    @NotEmpty(message = "At least one BookItemId must be provided")
    private List<Long> bookItemIds;
}
