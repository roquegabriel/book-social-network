package com.roque.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.roque.book.book.BookRepository;

@SpringBootTest
class BookNetworApiApplicationTests {

	@Autowired
	BookRepository repo;

	@Test
	void contextLoads() {
		
	}
}