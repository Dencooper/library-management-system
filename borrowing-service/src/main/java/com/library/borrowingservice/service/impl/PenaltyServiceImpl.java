package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.mapper.BorrowingMapper;
import com.library.borrowingservice.mapper.PenaltyMapper;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.Penalty;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.repository.PenaltyRepository;
import com.library.borrowingservice.service.IPenaltyService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements IPenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final BorrowingRepository borrowingRepository;
    private final PenaltyMapper penaltyMapper;
    private final BorrowingMapper borrowingMapper;
    private final AuthFeignClient authFeignClient;

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
    public List<PenaltyResponse> getUserPenalties() {
        List<Borrowing> borrowings = borrowingRepository.findAllByUserId(authFeignClient.fetchUser().getBody().getData().getId());
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

}
