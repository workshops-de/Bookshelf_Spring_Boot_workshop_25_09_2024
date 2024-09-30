package de.workshops.bookshelf.book;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class BookService {

    private final BookRepository bookRepository;

    List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        bookRepository.create(book);

        return book;
    }

    void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    Book searchBookByIsbn(String isbn) throws BookNotFoundException {
        return getAllBooks().stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow(BookNotFoundException::new);
    }

    Book searchBookByAuthor(String author) throws BookNotFoundException {
        return getAllBooks().stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow(BookNotFoundException::new);
    }

    List<Book> searchBooks(BookSearchRequest request) {
        return getAllBooks().stream()
                .filter(book -> hasAuthor(book, request.author()))
                .filter(book -> hasIsbn(book, request.isbn()))
                .toList();
    }

    private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }

    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }
}
