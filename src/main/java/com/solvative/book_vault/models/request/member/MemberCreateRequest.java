package com.solvative.book_vault.models.request.member;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.solvative.book_vault.enums.MembershipStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class MemberCreateRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotNull
    private MembershipStatus membershipStatus;


}
