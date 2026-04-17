package com.solvative.book_vault.models.request.loan;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class LoanCreateRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;


}
