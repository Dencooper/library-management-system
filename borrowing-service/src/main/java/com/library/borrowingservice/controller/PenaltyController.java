package com.library.borrowingservice.controller;

import com.library.borrowingservice.dto.response.penalty.PenaltyResponse;
import com.library.borrowingservice.model.Penalty;
import com.library.borrowingservice.service.IPenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public ResponseEntity<List<PenaltyResponse>> getPenaltyById(@RequestParam String email){
        return ResponseEntity.ok().body(iPenaltyService.getUserPenalties(email));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PenaltyResponse> updatePenalty(@PathVariable Long id, @RequestBody Penalty request){
        return ResponseEntity.ok().body(iPenaltyService.updatePenalty(id, request));
    }

}
