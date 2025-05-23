package com.library.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelfResponse {
    private Long id;
    private String code;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
