package com.solvative.book_vault.controller;


import com.solvative.book_vault.models.request.book.BookCreateRequest;
import com.solvative.book_vault.models.request.book.BookUpdateRequest;
import com.solvative.book_vault.models.response.ApiResponse;
import com.solvative.book_vault.models.response.book.BookResponse;
import com.solvative.book_vault.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ApiResponse<BookResponse> create(@Valid @RequestBody BookCreateRequest request) {
        return ApiResponse.success(bookService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(bookService.getById(id));
    }

    @GetMapping
    public ApiResponse<Page<BookResponse>> getAll(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Boolean available,
            Pageable pageable
    ) {
        return ApiResponse.success(bookService.search(genre, author, available, pageable));
    }

    @PutMapping("/{id}")
    public ApiResponse<BookResponse> update(@PathVariable Long id,
                                            @Valid @RequestBody BookUpdateRequest request) {
        return ApiResponse.success(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ApiResponse.success(null);
    }
}
