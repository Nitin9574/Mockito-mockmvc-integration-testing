package com.qualigy.mockitoexample.controller.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualigy.mockitoexample.controller.BookController;
import com.qualigy.mockitoexample.entity.Book;
import com.qualigy.mockitoexample.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerMvcTest {


    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    // create a mockmvc instance
    // instance with the provided bookController without starting the entire Spring context. This allows
    // you to focus specifically on testing the controller without involving other components.

    @Test
    void testGetAllBooks() throws Exception {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(
                new Book(1L, "pearson1", "Gosling-java"),
                new Book(2L, "The Cricket on the Hearth", "Charles Dickens")
        ));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("pearson1"))
                .andExpect(jsonPath("$[1].title").value("The Cricket on the Hearth"));

        // Verify that bookService.getAllBooks() is called once
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(new Book(12L, "The Daffodil Sky", "H. E. Bates")));

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Daffodil Sky"))
                .andExpect(jsonPath("$.author").value("H. E. Bates"));

        // Verify that bookService.getBookById(1L) is called once
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        // Arrange
        when(bookService.getBookById(1423423L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());

        // Verify that bookService.getBookById(1L) is called once
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testSaveBook() throws Exception {
        // Arrange
        Book bookToSave = new Book(null, "The India Story", "Bimal Jalal");
        Book savedBook = new Book(1L, "The India Story", "Bimal Jalal");

        when(bookService.saveBook(bookToSave)).thenReturn(savedBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("The India Story"))
                .andExpect(jsonPath("$.author").value("Bimal Jalal"));

        // Verify that bookService.saveBook(bookToSave) is called once
        verify(bookService, times(1)).saveBook(bookToSave);
    }

    @Test
    void testDeleteBook() throws Exception {
        // Arrange
        Long bookId = 132423L;

        // Act & Assert
        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());

        // Verify that bookService.deleteBook(bookId) is called once
        verify(bookService, times(1)).deleteBook(bookId);
    }
}
