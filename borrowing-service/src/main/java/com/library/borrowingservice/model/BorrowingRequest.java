package com.library.borrowingservice.model;

import com.library.borrowingservice.constant.BorrowingRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrowing_requests")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime requestedAt;

    private LocalDateTime acceptedAt;

    private Long librarianId;

    private LocalDateTime rejectedAt;

    @Enumerated(EnumType.STRING)
    private BorrowingRequestStatus status;

    private String description;

    @OneToMany(mappedBy = "borrowingRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowingRequestItem> items;
}

