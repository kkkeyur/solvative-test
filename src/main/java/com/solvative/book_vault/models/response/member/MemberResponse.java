package com.solvative.book_vault.models.response.member;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.solvative.book_vault.enums.MembershipStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private MembershipStatus membershipStatus;
    private LocalDateTime joinedAt;


}
