package com.library.borrowingservice.dto.request.penalty;

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
public class PenaltyCreationRequest {
    @NotNull
    private Long borrowingId;

    @NotBlank
    private String description;

    @NotNull
    private double amount;
}
