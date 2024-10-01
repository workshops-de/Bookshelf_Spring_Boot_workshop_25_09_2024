package de.workshops.bookshelf.book;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BookRepository extends ListCrudRepository<Book, Long> {

  Book findByIsbn(String isbn);

  Book findByAuthorContaining(String author);

  List<Book> findByIsbnAndAuthorContaining(String isbn, String author);
}
