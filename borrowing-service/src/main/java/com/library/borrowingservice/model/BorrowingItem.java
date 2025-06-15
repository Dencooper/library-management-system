package com.library.borrowingservice.model;

import com.library.commonservice.utils.constant.BookItemCondition;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "borrowing_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookItemId;

    @Enumerated(EnumType.STRING)
    private BookItemCondition bookItemCondition;

    @ManyToOne
    @JoinColumn(name = "borrowing_id")
    private Borrowing borrowing;
}
