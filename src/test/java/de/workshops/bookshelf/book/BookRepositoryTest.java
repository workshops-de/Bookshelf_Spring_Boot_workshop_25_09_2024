package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(
        includeFilters = {
            @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Repository.class
            ), @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = ConfigurationProperties.class
            )
        }
)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void create() {
        Book book = Book.builder()
                .title("Title")
                .author("Author")
                .description("Description")
                .isbn("123-4567890")
                .build();
        bookRepository.create(book);

        List<Book> books = bookRepository.findAll();

        assertNotNull(books);
        assertEquals(4, books.size());
        assertEquals(book.getIsbn(), books.get(3).getIsbn());

        // Restore previous state
        bookRepository.delete(book);
    }
}
