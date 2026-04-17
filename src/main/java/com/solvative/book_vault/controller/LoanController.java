package com.solvative.book_vault.controller;


import com.solvative.book_vault.models.request.loan.LoanCreateRequest;
import com.solvative.book_vault.models.response.ApiResponse;
import com.solvative.book_vault.models.response.loan.LoanResponse;
import com.solvative.book_vault.services.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;


    @PostMapping
    public ApiResponse<LoanResponse> borrow(@Valid @RequestBody LoanCreateRequest request) {
        return ApiResponse.success(loanService.borrowBook(request));
    }

    @PutMapping("/{id}/return")
    public ApiResponse<LoanResponse> returnBook(@PathVariable Long id, Authentication authentication) {
        return ApiResponse.success(loanService.returnBook(id, authentication));
    }

    @GetMapping("/overdue")
    public ApiResponse<Page<LoanResponse>> getOverdueLoans(Pageable pageable) {
        return ApiResponse.success(loanService.getOverdueLoans(pageable));
    }
}
