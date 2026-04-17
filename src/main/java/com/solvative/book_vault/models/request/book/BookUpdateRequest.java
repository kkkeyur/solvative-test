package com.solvative.book_vault.models.request.book;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class BookUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^978-\\d{10}$", message = "ISBN must match format 978-XXXXXXXXXX")
    private String isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genre;

    @Min(1)
    private int totalCopies;


}
