package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    List<Book> findAll() {
        String sql = "SELECT * FROM book";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }

    public void create(Book book) {
        String sql = "INSERT INTO book (title, description, author, isbn) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
            sql,
            book.getTitle(),
            book.getDescription(),
            book.getAuthor(),
            book.getIsbn()
        );
    }

    public void delete(Book book) {
        String sql = "DELETE FROM book WHERE isbn = ?";

        jdbcTemplate.update(
            sql,
            book.getIsbn()
        );
    }
}
