package com.solvative.book_vault.events;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class LoanOverdueEvent {

    private final Long loanId;
    private final Long memberId;
    private final Long bookId;


}
