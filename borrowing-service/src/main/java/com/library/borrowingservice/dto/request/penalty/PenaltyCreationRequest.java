package com.library.borrowingservice.dto.request.penalty;

import com.library.borrowingservice.constant.BookItemReturnCondition;
import jakarta.validation.constraints.NotBlank;
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
public class PenaltyCreationRequest {
    @NotNull
    private Long borrowingId;

    @NotBlank
    private String description;

    @NotNull
    private double amount;
}
