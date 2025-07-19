package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyStatisticsResponse;
import com.library.borrowingservice.mapper.PenaltyMapper;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.BorrowingItem;
import com.library.borrowingservice.model.Penalty;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.repository.PenaltyRepository;
import com.library.borrowingservice.service.IPenaltyService;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.utils.constant.BookItemCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements IPenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final BorrowingRepository borrowingRepository;
    private final PenaltyMapper penaltyMapper;
    private final UsersFeignClient usersFeignClient;
    private final BooksFeignClient booksFeignClient;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public PenaltyResponse createPenalty(PenaltyCreationRequest request) {
        Borrowing borrowing = borrowingRepository.findById(request.getBorrowingId())
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));
        Penalty penalty = Penalty.builder()
                .amount(request.getAmount())
                .penaltyAt(LocalDateTime.now())
                .isPaid(false)
                .borrowing(borrowing)
                .build();
        if(getPenaltiesByUserId(borrowing.getUserId()).size() >= 2){
            usersFeignClient.updateBannedUser(borrowing.getUserId(), true);
        }
        return penaltyMapper.toResponse(penaltyRepository.save(penalty));
    }

    @Override
    @Transactional(readOnly = true)
    public PenaltyResponse getPenaltyById(Long id) {
        Penalty penalty = penaltyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Penalty " + id + " not found"));
        return penaltyMapper.toResponse(penalty);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public List<PenaltyResponse> getAllPenalties() {
        return penaltyRepository.findAll().stream()
                .map(penaltyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltyResponse> getPenaltiesByUserId(Long id) {
        List<Borrowing> borrowings = borrowingRepository.findAllByUserId(id);
        return  penaltyRepository.findAllByBorrowingIn(borrowings)
                .stream()
                .map(penaltyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public PenaltyResponse updatePenalty(Long id, Penalty request) {
        Penalty penalty = penaltyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Penalty " + id + " not found"));
        penalty.setDescription(request.getDescription());
        penalty.setIsPaid(request.getIsPaid());
        penaltyRepository.save(penalty);
        return penaltyMapper.toResponse(penalty);
    }

    @Override
    public PenaltyStatisticsResponse doPenaltyStatistic(LocalDateTime from, LocalDateTime to) {
        List<Penalty> penalties = penaltyRepository.findAllByPenaltyAtBetween(from, to);
        double totalPenalties = 0;
        double collectedPenalties = 0;
        double outstandingPenalties = 0;
        double damageFees = 0;
        double lostBookFees = 0;
        int outstandingCount = 0;
        int damageCount = 0;
        int lostCount = 0;
        for(Penalty penalty : penalties) {
            for(BorrowingItem borrowingItem : penalty.getBorrowing().getItems()){
                BookItemResponse bookItemResponse = booksFeignClient.getBookItemById(borrowingItem.getBookItemId()).getBody().getData();
                if(bookItemResponse.getBookItemCondition() == BookItemCondition.MAJOR_DAMAGE) {
                    damageFees += bookItemResponse.getPrice();
                    damageCount += 1;
                } else if(bookItemResponse.getBookItemCondition() == BookItemCondition.LOST) {
                    lostBookFees += bookItemResponse.getPrice();
                    lostCount += 1;
                }
            }

            if(penalty.getIsPaid()) {
                collectedPenalties += penalty.getAmount();
            } else {
                outstandingPenalties += penalty.getAmount();
                outstandingCount += 1;
            }
            totalPenalties += penalty.getAmount();
        }

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);
        YearMonth twoMonthsAgo = currentMonth.minusMonths(2);

        LocalDateTime start1 = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime end1 = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);
        List<Penalty> currentPenalties = penaltyRepository.findAllByPenaltyAtBetween(start1, end1);

        LocalDateTime start2 = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime end2 = lastMonth.atEndOfMonth().atTime(LocalTime.MAX);
        List<Penalty> lastPenalties = penaltyRepository.findAllByPenaltyAtBetween(start2, end2);

        LocalDateTime start3 = twoMonthsAgo.atDay(1).atStartOfDay();
        LocalDateTime end3 = twoMonthsAgo.atEndOfMonth().atTime(LocalTime.MAX);
        List<Penalty> twoMonthsAgoPenalties = penaltyRepository.findAllByPenaltyAtBetween(start3, end3);

        PenaltyStatisticsResponse.MonthlyDataDTO currentMonthData = calculateMonthlyData(currentPenalties, currentMonth);
        PenaltyStatisticsResponse.MonthlyDataDTO lastMonthData = calculateMonthlyData(lastPenalties, lastMonth);
        PenaltyStatisticsResponse.MonthlyDataDTO twoMonthsAgoData = calculateMonthlyData(twoMonthsAgoPenalties, twoMonthsAgo);

        return PenaltyStatisticsResponse.builder()
                .totalPenalties(totalPenalties)
                .collectedPenalties(collectedPenalties)
                .outstandingPenalties(outstandingPenalties)
                .penaltyCount(penalties.size())
                .outstandingCount(outstandingCount)
                .collectionRate(((double) collectedPenalties /totalPenalties) * 100)
                .avgPenaltyAmount((double) totalPenalties /penalties.size())
                .penaltyBreakdown(PenaltyStatisticsResponse.PenaltyBreakdownDTO.builder()
                        .damageCount(damageCount)
                        .damageFees(damageFees)
                        .lostCount(lostCount)
                        .lostBookFees(lostBookFees)
                        .build())
                .monthlyData(List.of(currentMonthData, lastMonthData, twoMonthsAgoData))
                .build();
    }

    private PenaltyStatisticsResponse.MonthlyDataDTO calculateMonthlyData(List<Penalty> penalties, YearMonth month) {
        double total = 0;
        double collected = 0;
        double outstanding = 0;

        for (Penalty penalty : penalties) {
            double amount = penalty.getAmount();
            total +=  amount;
            if (penalty.getIsPaid()) {
                collected += amount;
            } else {
                outstanding += amount;
            }
        }

        return PenaltyStatisticsResponse.MonthlyDataDTO.builder()
                .month(month)
                .totalPenalties(total)
                .collectedPenalties(collected)
                .outstandingPenalties(outstanding)
                .build();
    }

}
