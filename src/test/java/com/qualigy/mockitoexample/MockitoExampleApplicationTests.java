package com.qualigy.mockitoexample;

import com.qualigy.mockitoexample.repository.BookRepository;
import com.qualigy.mockitoexample.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MockitoExampleApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@Test
	void testGetAllBooks() {

	}

}
