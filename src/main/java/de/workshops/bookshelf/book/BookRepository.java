package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
class BookRepository {

    private final ObjectMapper mapper;

    private final ResourceLoader resourceLoader;

    private List<Book> books;

    @PostConstruct
    void initBookList() throws IOException {
        this.books = mapper.readValue(
            resourceLoader
                .getResource("classpath:books.json")
                .getInputStream(),
            new TypeReference<>() {}
        );
    }

    List<Book> findAllBooks() {
        return books;
    }
}
