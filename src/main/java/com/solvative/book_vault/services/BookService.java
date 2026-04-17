package com.solvative.book_vault.services;


import com.solvative.book_vault.entitites.Book;
import com.solvative.book_vault.excp.BusinessException;
import com.solvative.book_vault.excp.ResourceNotFoundException;
import com.solvative.book_vault.models.mappers.BookMapper;
import com.solvative.book_vault.models.request.book.BookCreateRequest;
import com.solvative.book_vault.models.request.book.BookUpdateRequest;
import com.solvative.book_vault.models.response.book.BookResponse;
import com.solvative.book_vault.repos.BookRepository;
import com.solvative.book_vault.repos.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;



    @Transactional
    @CacheEvict(value = "bookCatalogue", allEntries = true)
    public BookResponse create(BookCreateRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("Book with ISBN already exists");
        }

        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return bookMapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "bookCatalogue", key = "T(java.util.Objects).hash(#genre, #author, #available, #pageable.pageNumber, #pageable.pageSize, #pageable.sort.toString())")
    public Page<BookResponse> search(String genre, String author, Boolean available, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecification.hasGenre(genre))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.isAvailable(available));

        return bookRepository.findAll(spec, pageable).map(bookMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "bookCatalogue", allEntries = true)
    public BookResponse update(Long id, BookUpdateRequest request) {
        Book book = findEntity(id);

        if (bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new BusinessException("Another book with this ISBN already exists");
        }

        int activeLoansImpact = book.getTotalCopies() - book.getAvailableCopies();
        if (request.getTotalCopies() < activeLoansImpact) {
            throw new BusinessException("totalCopies cannot be lower than currently borrowed copies");
        }

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies() - activeLoansImpact);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    @CacheEvict(value = "bookCatalogue", allEntries = true)
    public void delete(Long id) {
        Book book = findEntity(id);
        bookRepository.delete(book);
    }

    public Book findEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }
}
