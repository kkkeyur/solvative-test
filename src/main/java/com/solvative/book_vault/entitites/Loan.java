package com.solvative.book_vault.entitites;

import com.solvative.book_vault.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Loans")
@Setter
@Getter
@RequiredArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @PrePersist
    public void prePersist() {

        if (this.borrowedAt == null) {
            this.borrowedAt = LocalDateTime.now();
        }
        if (this.dueDate == null) {
            this.dueDate = this.borrowedAt.plusDays(14);
        }
        if (this.status == null) {
            this.status = LoanStatus.ACTIVE;
        }
    }


}
