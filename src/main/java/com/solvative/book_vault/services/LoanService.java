package com.solvative.book_vault.services;


import com.solvative.book_vault.entitites.Book;
import com.solvative.book_vault.entitites.Loan;
import com.solvative.book_vault.entitites.Member;
import com.solvative.book_vault.enums.LoanStatus;
import com.solvative.book_vault.enums.MembershipStatus;
import com.solvative.book_vault.events.LoanOverdueEvent;
import com.solvative.book_vault.excp.BusinessException;
import com.solvative.book_vault.excp.ResourceNotFoundException;
import com.solvative.book_vault.excp.UnauthorizedActionException;
import com.solvative.book_vault.models.mappers.LoanMapper;
import com.solvative.book_vault.models.request.loan.LoanCreateRequest;
import com.solvative.book_vault.models.response.loan.LoanResponse;
import com.solvative.book_vault.repos.BookRepository;
import com.solvative.book_vault.repos.LoanRepository;
import com.solvative.book_vault.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LoanResponse borrowBook(LoanCreateRequest request) {
        Book book = bookService.findEntity(request.getBookId());
        Member member = memberService.findEntity(request.getMemberId());

        if (member.getMembershipStatus() == MembershipStatus.SUSPENDED) {
            throw new BusinessException("Suspended members cannot borrow books");
        }

        long activeLoanCount = loanRepository.countByMemberIdAndStatus(member.getId(), LoanStatus.ACTIVE);
        if (activeLoanCount >= 3) {
            throw new BusinessException("A member may not have more than 3 active loans");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No available copies for this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);

        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toResponse(savedLoan);
    }

    @Transactional
    public LoanResponse returnBook(Long loanId, Authentication authentication) {
        Loan loan = findEntityWithRelations(loanId);

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException("Loan is already returned");
        }

        if (SecurityUtil.isMember(authentication)) {
            Long currentMemberId = SecurityUtil.extractMemberId(authentication);
            if (!loan.getMember().getId().equals(currentMemberId)) {
                throw new UnauthorizedActionException("Members can only return their own books");
            }
        }

        loan.setReturnedAt(LocalDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toResponse(savedLoan);
    }

    @Transactional(readOnly = true)
    public Page<LoanResponse> getOverdueLoans(Pageable pageable) {
        return loanRepository.findByDueDateBeforeAndStatus(LocalDateTime.now(), LoanStatus.ACTIVE, pageable)
                .map(loan -> {
                    publishOverdueEventIfNeeded(loan);
                    return loanMapper.toResponse(loan);
                });
    }

    @Transactional
    public void markOverdueLoans() {
        loanRepository.findByDueDateBeforeAndStatus(LocalDateTime.now(), LoanStatus.ACTIVE)
                .forEach(loan -> {
                    loan.setStatus(LoanStatus.OVERDUE);
                    loanRepository.save(loan);
                    eventPublisher.publishEvent(
                            new LoanOverdueEvent(
                                    loan.getId(),
                                    loan.getMember().getId(),
                                    loan.getBook().getId()
                            )
                    );
                });
    }

    public Loan findEntityWithRelations(Long id) {
        return loanRepository.findWithBookAndMemberById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
    }

    private void publishOverdueEventIfNeeded(Loan loan) {
        if (loan.getDueDate().isBefore(LocalDateTime.now())) {
            eventPublisher.publishEvent(
                    new LoanOverdueEvent(
                            loan.getId(),
                            loan.getMember().getId(),
                            loan.getBook().getId()
                    )
            );
        }
    }
}
