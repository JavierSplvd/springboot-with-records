package com.numian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.numian.bookstore.controllers.BookController;
import com.numian.bookstore.dto.BookDto;
import com.numian.bookstore.entities.Book;
import com.numian.bookstore.infrastructure.BookRepository;

@SpringBootTest
public class BookControllerTests {

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookController bookController;

    private Book book = new Book(1L, "Title 1", "Author 1", "ISBN-1234");

    @Test
    public void testGetAllBooks() throws Exception {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
                book,
                new Book(2L, "Title 2", "Author 2", "ISBN-5678")));
        List<BookDto> allBooks = bookController.getAllBooks();
        assertEquals(allBooks.size(), 2);
        assertEquals(allBooks.get(0).author(), "Author 1");
        assertEquals(allBooks.get(1).author(), "Author 2");
    }

    @Test
    public void testGetBookById() throws Exception {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(
                        Optional.of(book));
        BookDto book = bookController.getBookById(1L).getBody();
        assertEquals(book.author(), "Author 1");
    }

    @Test
    public void testCreateBook() throws Exception {
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(book);
        BookDto book = bookController.createBook(new BookDto(1L, "Title 1", "Author 1", "ISBN-1234")).getBody();
        assertEquals(book.author(), "Author 1");
    }

    @Test
    public void testUpdateBook() throws Exception {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(
                        Optional.of(book));
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(book);
        BookDto b = bookController.updateBook(new BookDto(1L, "Title 1", "Author 1", "ISBN-1234"), 1L).getBody();
        assertEquals(b.author(), "Author 1");
    }

    @Test
    public void testDeleteBook() throws Exception {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(
                        Optional.of(book));
        bookController.deleteBookById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }
}
