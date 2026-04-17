package com.solvative.book_vault.repos;


import com.solvative.book_vault.entitites.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public final class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> hasGenre(String genre) {
        return (root, query, cb) ->
                genre == null || genre.isBlank()
                        ? cb.conjunction()
                        : cb.equal(cb.lower(root.get("genre")), genre.toLowerCase());
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) ->
                author == null || author.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> isAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) {
                return cb.conjunction();
            }
            return available
                    ? cb.greaterThan(root.get("availableCopies"), 0)
                    : cb.equal(root.get("availableCopies"), 0);
        };
    }
}
