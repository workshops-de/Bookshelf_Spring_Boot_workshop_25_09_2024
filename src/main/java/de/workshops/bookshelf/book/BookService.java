package de.workshops.bookshelf.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BookService {

  private final BookRepository bookRepository;

  List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book createBook(Book book) {
    bookRepository.save(book);

    return book;
  }

  void deleteBook(Book book) {
    bookRepository.delete(book);
  }

  Book searchBookByIsbn(String isbn) throws BookNotFoundException {
    final var book = bookRepository.findByIsbn(isbn);
    if (book == null) {
      throw new BookNotFoundException();
    }

    return book;
  }

  Book searchBookByAuthor(String author) throws BookNotFoundException {
    final var book = bookRepository.findByAuthorContaining(author);
    if (book == null) {
      throw new BookNotFoundException();
    }

    return book;
  }

  List<Book> searchBooks(BookSearchRequest request) {
    return bookRepository.findByIsbnAndAuthorContaining(
        request.isbn(),
        request.author()
    );
  }
}
