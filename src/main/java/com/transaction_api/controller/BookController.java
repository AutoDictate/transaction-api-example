package com.transaction_api.controller;

import com.transaction_api.model.Book;
import com.transaction_api.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping("/add/{isbn}")
    public ResponseEntity<Book> createUpdateBook(
            @PathVariable final String isbn,
            @RequestBody final Book book
    ) {
        book.setIsbn(isbn);
        final boolean isBookExists = service.isBookExists(book);
        final Book savedBook = service.save(book);

        if (isBookExists) {
            return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
        }else {
            return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Iterable<Book>> getAllBooks() {
        return new ResponseEntity<>(service.listBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        final Optional<Book> book = service.findById(id);

        return book.map(b -> new ResponseEntity<Book>(b, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        service.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
