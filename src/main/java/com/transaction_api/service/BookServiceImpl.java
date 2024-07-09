package com.transaction_api.service;

import com.transaction_api.model.Author;
import com.transaction_api.model.Book;
import com.transaction_api.repository.AuthorRepository;
import com.transaction_api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorRepository authorRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean isBookExists(Book book) {
        return bookRepository.existsById(book.getIsbn());
    }

    @Transactional
    @Override
    public Book save(final Book book) {

        // 1. Check the author is null
        if (book.getAuthor() == null) {
            throw new RuntimeException("Author must be provided");
        }

        // If the author is present, get the author and store it
        final Author bookAuthor = book.getAuthor();

        // 2. Save the book without author
        book.setAuthor(null);
        final Book savedBook = bookRepository.save(book);

        // 3. Create / update Author
        final Author author;

        // Provided author is not in a database,
        // we will save the author as New Author in database
        if (bookAuthor.getId() == null) {
            author = authorRepository.save(bookAuthor);
        } else {
        // if Author is already in database, find it.
            author = authorRepository.findById(bookAuthor.getId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
        }

        // 4. save the author in saved book
        savedBook.setAuthor(author);

        // 5. save the book with author
        return bookRepository.save(savedBook);
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Iterable<Book> listBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteBookById(String isbn) {
        try {
            bookRepository.deleteById(isbn);
        } catch (EmptyResultDataAccessException e) {
            log.debug("The book you're trying to delete is not existed", e);
        }
    }
}
