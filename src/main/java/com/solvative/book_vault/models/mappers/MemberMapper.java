package com.solvative.book_vault.models.mappers;


import com.solvative.book_vault.entitites.Member;
import com.solvative.book_vault.models.response.member.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberResponse toResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setEmail(member.getEmail());
        response.setName(member.getName());
        response.setMembershipStatus(member.getMembershipStatus());
        response.setJoinedAt(member.getJoinedAt());
        return response;
    }
}
