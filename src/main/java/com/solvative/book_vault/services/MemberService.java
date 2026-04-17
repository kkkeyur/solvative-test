package com.solvative.book_vault.services;


import com.solvative.book_vault.entitites.Member;
import com.solvative.book_vault.excp.BusinessException;
import com.solvative.book_vault.excp.ResourceNotFoundException;
import com.solvative.book_vault.excp.UnauthorizedActionException;
import com.solvative.book_vault.models.mappers.LoanMapper;
import com.solvative.book_vault.models.mappers.MemberMapper;
import com.solvative.book_vault.models.request.member.MemberCreateRequest;
import com.solvative.book_vault.models.request.member.MemberUpdateRequest;
import com.solvative.book_vault.models.response.loan.LoanResponse;
import com.solvative.book_vault.models.response.member.MemberResponse;
import com.solvative.book_vault.repos.LoanRepository;
import com.solvative.book_vault.repos.MemberRepository;
import com.solvative.book_vault.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final MemberMapper memberMapper;
    private final LoanMapper loanMapper;

    @Transactional
    public MemberResponse create(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Member with email already exists");
        }

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setMembershipStatus(request.getMembershipStatus());

        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        return memberMapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<MemberResponse> getAll(Pageable pageable) {
        return memberRepository.findAll(pageable).map(memberMapper::toResponse);
    }

    @Transactional
    public MemberResponse update(Long id, MemberUpdateRequest request) {
        Member member = findEntity(id);

        if (memberRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BusinessException("Another member with email already exists");
        }

        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setMembershipStatus(request.getMembershipStatus());

        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.delete(findEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<MemberResponse> search(String q, Pageable pageable) {
        return memberRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable)
                .map(memberMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> getLoanHistory(Long memberId, Authentication authentication) {
        if (SecurityUtil.isMember(authentication)) {
            Long authenticatedMemberId = SecurityUtil.extractMemberId(authentication);
            if (!memberId.equals(authenticatedMemberId)) {
                throw new UnauthorizedActionException("Members can only view their own loans");
            }
        }

        findEntity(memberId);
        return loanRepository.findByMemberIdOrderByBorrowedAtDesc(memberId)
                .stream()
                .map(loanMapper::toResponse)
                .toList();
    }

    public Member findEntity(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + id));
    }
}
