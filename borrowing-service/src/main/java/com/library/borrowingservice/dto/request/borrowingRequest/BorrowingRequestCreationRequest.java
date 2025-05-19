package com.library.borrowingservice.dto.request.borrowingRequest;

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
public class BorrowingRequestCreationRequest {
    @NotEmpty(message = "At least one bookId must be provided")
    private List<Long> bookIds;
}
