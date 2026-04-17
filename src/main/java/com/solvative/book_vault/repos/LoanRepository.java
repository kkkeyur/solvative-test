package com.solvative.book_vault.repos;


import com.solvative.book_vault.entitites.Loan;
import com.solvative.book_vault.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @EntityGraph(attributePaths = {"book", "member"})
    Optional<Loan> findWithBookAndMemberById(Long id);

    @EntityGraph(attributePaths = {"book", "member"})
    List<Loan> findByMemberIdOrderByBorrowedAtDesc(Long memberId);

    long countByMemberIdAndStatus(Long memberId, LoanStatus status);

    @EntityGraph(attributePaths = {"book", "member"})
    Page<Loan> findByDueDateBeforeAndStatus(LocalDateTime dueDate, LoanStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member"})
    List<Loan> findByDueDateBeforeAndStatus(LocalDateTime dueDate, LoanStatus status);
}
