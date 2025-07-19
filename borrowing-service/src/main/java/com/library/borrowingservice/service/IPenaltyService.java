package com.library.borrowingservice.service;

import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.borrowing.UserStatisticsResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyStatisticsResponse;
import com.library.borrowingservice.model.Penalty;

import java.time.LocalDateTime;
import java.util.List;

public interface IPenaltyService {
    PenaltyResponse createPenalty(PenaltyCreationRequest request);
    PenaltyResponse getPenaltyById(Long id);
    List<PenaltyResponse> getAllPenalties();
    List<PenaltyResponse> getPenaltiesByUserId(Long id);
    PenaltyResponse updatePenalty(Long id, Penalty request);
    PenaltyStatisticsResponse doPenaltyStatistic(LocalDateTime from, LocalDateTime to);
}
