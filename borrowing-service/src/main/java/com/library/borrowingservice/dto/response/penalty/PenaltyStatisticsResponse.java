package com.library.borrowingservice.dto.response.penalty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyStatisticsResponse {
    private double totalPenalties;
    private double collectedPenalties;
    private double outstandingPenalties;
    private int penaltyCount;
    private int outstandingCount;
    private double collectionRate;
    private double avgPenaltyAmount;
    private PenaltyBreakdownDTO penaltyBreakdown;
    private List<MonthlyDataDTO> monthlyData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PenaltyBreakdownDTO {
        private int damageCount;
        private double damageFees;
        private int lostCount;
        private double lostBookFees;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyDataDTO {
        private YearMonth month;
        private double totalPenalties;
        private double collectedPenalties;
        private double outstandingPenalties;
    }
}
