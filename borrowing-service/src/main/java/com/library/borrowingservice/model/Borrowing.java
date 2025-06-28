package com.library.borrowingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrowings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long librarianId;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private Boolean isLate;

    private String description;

    @OneToMany(mappedBy = "borrowing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowingItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "borrowing", cascade = CascadeType.ALL)
    private Penalty penalty;
}
