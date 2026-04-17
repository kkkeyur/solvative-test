package com.solvative.book_vault.entitites;


import com.solvative.book_vault.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Members")
@Setter
@Getter
@RequiredArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus membershipStatus;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {

        if (this.joinedAt == null) {
            this.joinedAt = LocalDateTime.now();
        }
        if (this.membershipStatus == null) {
            this.membershipStatus = MembershipStatus.ACTIVE;
        }
    }

}