package com.library.borrowingservice.controller;

import com.library.borrowingservice.dto.response.borrowing.UserStatisticsResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.dto.response.penalty.PenaltyStatisticsResponse;
import com.library.borrowingservice.model.Penalty;
import com.library.borrowingservice.service.IPenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/penalties")
@RequiredArgsConstructor
public class PenaltyController {
    private final IPenaltyService iPenaltyService;

    @GetMapping("/{id}")
    public ResponseEntity<PenaltyResponse> getPenaltyById(@PathVariable Long id){
        return ResponseEntity.ok().body(iPenaltyService.getPenaltyById(id));
    }

    @GetMapping
    public ResponseEntity<List<PenaltyResponse>> getAllPenalties(){
        return ResponseEntity.ok().body(iPenaltyService.getAllPenalties());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PenaltyResponse>> getPenaltyByUserId(@PathVariable Long id){
        return ResponseEntity.ok().body(iPenaltyService.getPenaltiesByUserId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PenaltyResponse> updatePenalty(@PathVariable Long id, @RequestBody Penalty request){
        return ResponseEntity.ok().body(iPenaltyService.updatePenalty(id, request));
    }

    @GetMapping("/statistic")
    public ResponseEntity<PenaltyStatisticsResponse> getPenaltyStat(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ){
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atStartOfDay();
        return ResponseEntity.ok().body(iPenaltyService.doPenaltyStatistic(from, to));
    }

}
