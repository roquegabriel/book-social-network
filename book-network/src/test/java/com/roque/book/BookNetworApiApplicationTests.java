package com.roque.book;

import static org.assertj.core.api.Assertions.assertThat;

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