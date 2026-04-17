package com.solvative.book_vault.models.mappers;


import com.solvative.book_vault.entitites.Loan;
import com.solvative.book_vault.models.response.loan.LoanResponse;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setBookId(loan.getBook().getId());
        response.setBookTitle(loan.getBook().getTitle());
        response.setMemberId(loan.getMember().getId());
        response.setMemberName(loan.getMember().getName());
        response.setBorrowedAt(loan.getBorrowedAt());
        response.setDueDate(loan.getDueDate());
        response.setReturnedAt(loan.getReturnedAt());
        response.setStatus(loan.getStatus());
        return response;
    }
}
