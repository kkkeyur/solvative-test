package com.solvative.book_vault.controller;


import com.solvative.book_vault.models.request.member.MemberCreateRequest;
import com.solvative.book_vault.models.request.member.MemberUpdateRequest;
import com.solvative.book_vault.models.response.ApiResponse;
import com.solvative.book_vault.models.response.loan.LoanResponse;
import com.solvative.book_vault.models.response.member.MemberResponse;
import com.solvative.book_vault.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ApiResponse<MemberResponse> create(@Valid @RequestBody MemberCreateRequest request) {
        return ApiResponse.success(memberService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(memberService.getById(id));
    }

    @GetMapping
    public ApiResponse<Page<MemberResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(memberService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody MemberUpdateRequest request) {
        return ApiResponse.success(memberService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/search")
    public ApiResponse<Page<MemberResponse>> search(@RequestParam String q, Pageable pageable) {
        return ApiResponse.success(memberService.search(q, pageable));
    }

    @GetMapping("/{id}/loans")
    public ApiResponse<List<LoanResponse>> getLoanHistory(@PathVariable Long id, Authentication authentication) {
        return ApiResponse.success(memberService.getLoanHistory(id, authentication));
    }
}
