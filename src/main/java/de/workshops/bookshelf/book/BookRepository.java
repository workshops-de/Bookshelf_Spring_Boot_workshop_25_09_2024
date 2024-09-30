package de.workshops.bookshelf.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
