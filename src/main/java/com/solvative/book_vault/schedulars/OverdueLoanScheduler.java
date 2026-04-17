package com.solvative.book_vault.schedulars;


import com.solvative.book_vault.services.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OverdueLoanScheduler {

    private final LoanService loanService;

    @Scheduled(cron = "0 0 1 * * *")
    public void scanAndMarkOverdueLoans() {
        loanService.markOverdueLoans();
    }
}
