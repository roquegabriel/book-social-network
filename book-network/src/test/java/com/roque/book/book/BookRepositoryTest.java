package com.roque.book.book;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.roque.book.user.User;


@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    // @BeforeEach
    // public void setup(){
    //     // Given
    //     bookRepository.save(Book.builder().id(10).title("Testing").authorName("Me").isbn("1234325512").synopsis("This is a test").bookCover("test.jpg").isArchived(false).isShareable(true).createdDate(LocalDateTime.now()).createdBy(102).owner(User.builder().id(102).build()).build());
    // }

    @Test
    void should_find_owner_book(){
        
        // When
        Optional<Book> book = bookRepository.findById(2);
        
        // Then
        assertTrue(book.isPresent());
        assertTrue(book.get().getOwner().getId() == 102);
    }


}
