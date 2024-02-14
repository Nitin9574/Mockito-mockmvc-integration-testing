package com.qualigy.mockitoexample.controller.integration;

import com.qualigy.mockitoexample.entity.Book;
import com.qualigy.mockitoexample.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookService bookService;

    @Test
    void testGetAllBooks() {
        // Arrange & Act
        Book[] books = restTemplate.getForObject("http://localhost:" + port + "/api/books", Book[].class);

        // Assert
        assertEquals(6, books.length);
        assertEquals("The greats by", books[0].getTitle());
        assertEquals("microsoft", books[1].getTitle());
        assertEquals("Oracle", books[2].getTitle());
        assertEquals("spring", books[3].getTitle());
        assertEquals("node js", books[4].getTitle());
        assertEquals("nodejs1", books[5].getTitle());

    }


    @Test
    void testGetBookById() {
        // Arrange
        Book savedBook = bookService.saveBook(new Book(null, "The greats by", "Gosling"));

        // Act
        Book book = restTemplate.getForObject("http://localhost:" + port + "/api/books/{id}", Book.class, savedBook.getId());

        // Assert
        assertEquals(savedBook.getTitle(), book.getTitle());
        assertEquals(savedBook.getAuthor(), book.getAuthor());
    }

    @Test
    void testGetBookByIdNotFound() {
        // Act
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/books/{id}", String.class, 999);

        // Assert
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Not Found", responseEntity.getBody());
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book bookToSave = new Book(22l, "Java Programming", "James");

        // Act
        ResponseEntity<Book> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/books", bookToSave, Book.class);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        Book savedBook = responseEntity.getBody();
        assertEquals(bookToSave.getTitle(), savedBook.getTitle());
        assertEquals(bookToSave.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book savedBook = bookService.saveBook(new Book(22l, "Java Programming", "James"));

        // Act
        restTemplate.delete("http://localhost:" + port + "/api/books/{id}", savedBook.getId());

        // Assert
        Book deletedBook = bookService.getBookById(savedBook.getId()).orElse(null);
        assertEquals(null, deletedBook);
    }

//    @Test
//    void testGetBookById() throws Exception {
//        when(bookService.getBookById(1L)).thenReturn(Optional.of(new Book(1L, "Effective Java", "Joshua Bloch")));
//
//        mockMvc.perform(get("/api/books/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Effective Java"))
//                .andExpect(jsonPath("$.author").value("Joshua Bloch"));
//
//        verify(bookService, times(1)).getBookById(1L);
//    }
//
//    @Test
//    void testSaveBook() throws Exception {
//        Book bookToSave = new Book(null, "Clean Code", "Robert C. Martin");
//        Book savedBook = new Book(1L, "Clean Code", "Robert C. Martin");
//
//        when(bookService.saveBook(bookToSave)).thenReturn(savedBook);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookToSave)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.title").value("Clean Code"))
//                .andExpect(jsonPath("$.author").value("Robert C. Martin"));
//
//        String response = resultActions.andReturn().getResponse().getContentAsString();
//        Book responseBook = objectMapper.readValue(response, Book.class);
//
//        assertEquals(savedBook, responseBook);
//
//        verify(bookService, times(1)).saveBook(bookToSave);
//    }
//
//    @Test
//    void testDeleteBook() throws Exception {
//        Long bookId = 1L;
//
//        mockMvc.perform(delete("/api/books/{id}", bookId))
//                .andExpect(status().isNoContent());
//
//        verify(bookService, times(1)).deleteBook(bookId);
//    }
}
