package com.numian.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.numian.bookstore.entities.Book;
import com.numian.bookstore.infrastructure.BookRepository;

@WebAppConfiguration
@SpringBootTest
public class BookControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() throws Exception {
        bookRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        var getRequest = MockMvcRequestBuilders.get("/api/books").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book b = bookRepository.save(new Book("Title", "Author", "ISBN-1234"));

        var getRequest = MockMvcRequestBuilders.get("/api/books/" + b.getId()).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(b.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(b.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(b.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(b.getIsbn()));
    }

    @Test
    public void testCreateBook() throws Exception {
        var postRequest = MockMvcRequestBuilders.post("/api/books").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Title\",\"author\":\"Author\",\"isbn\":\"ISBN-1234\"}");
        mockMvc.perform(postRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("ISBN-1234"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book b = bookRepository.save(new Book("Title", "Author", "ISBN-1234"));

        var putRequest = MockMvcRequestBuilders.put("/api/books/" + b.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Title2\",\"author\":\"Author2\",\"isbn\":\"ISBN-5678\"}");
        mockMvc.perform(putRequest).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteBook() throws Exception {
        Book b = bookRepository.save(new Book("Title", "Author", "ISBN-1234"));

        var deleteRequest = MockMvcRequestBuilders.delete("/api/books/" + b.getId());
        mockMvc.perform(deleteRequest).andExpect(MockMvcResultMatchers.status().isOk());

        long count = bookRepository.findAll().stream().count();
        assertEquals(0, count);
    }

}
