package com.library.authservice.model;

import com.library.authservice.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;
}
