package com.library.borrowingservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "borrowing_request_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "borrowing_request_id")
    private BorrowingRequest borrowingRequest;
}
