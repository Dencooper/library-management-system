package com.library.borrowingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.security.BasicPermission;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalties")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "borrowing_id", nullable = false)
    private Borrowing borrowing;

    @Column(nullable = false)
    private double amount;

    private String description;

    private LocalDateTime penaltyAt;

    private Boolean isPaid;
}
