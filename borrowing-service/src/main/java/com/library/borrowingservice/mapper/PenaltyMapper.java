package com.library.borrowingservice.mapper;

import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.model.Penalty;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PenaltyMapper {
    private final UsersFeignClient usersFeignClient;

    public PenaltyResponse toResponse(Penalty penalty) {
        UserResponse user = usersFeignClient.getUserById(penalty.getBorrowing().getUserId()).getBody().getData();
        return PenaltyResponse.builder()
                .id(penalty.getId())
                .amount(penalty.getAmount())
                .description(penalty.getDescription())
                .penaltyAt(LocalDateTime.now())
                .isPaid(penalty.getIsPaid())
                .user(user)
                .borrowingId(penalty.getBorrowing().getId())
                .build();
    }
}
