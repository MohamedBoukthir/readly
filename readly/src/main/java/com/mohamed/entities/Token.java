package com.mohamed.entities;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
//@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
