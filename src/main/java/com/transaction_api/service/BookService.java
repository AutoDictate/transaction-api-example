package com.transaction_api.service;

import com.transaction_api.model.Book;

import java.util.Optional;

public interface BookService {

    boolean isBookExists(Book book);

    Book save(Book book);

    Optional<Book> findById(String id);

    Iterable<Book> listBooks();

    void deleteBookById(String isbn);
}
