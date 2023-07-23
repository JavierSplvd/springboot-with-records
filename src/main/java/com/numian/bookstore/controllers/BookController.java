package com.numian.bookstore.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numian.bookstore.dto.BookDto;
import com.numian.bookstore.entities.Book;
import com.numian.bookstore.infrastructure.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("")
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(this::toRecord).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toRecord(book.get()));
    }

    @PostMapping("")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        Book book = bookRepository.save(new Book(bookDto.title(), bookDto.author(), bookDto.isbn()));
        return ResponseEntity.ok(toRecord(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto, @PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Book b = bookRepository.save(new Book(id, bookDto.title(), bookDto.author(), bookDto.isbn()));
        return ResponseEntity.ok(toRecord(b));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private BookDto toRecord(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn());
    }
}
