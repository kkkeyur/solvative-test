package com.solvative.book_vault.models.response.book;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int totalCopies;
    private int availableCopies;


}
