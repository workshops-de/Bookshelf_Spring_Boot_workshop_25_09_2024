package de.workshops.bookshelf.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
class BookNotFoundException extends Exception {

  BookNotFoundException() {
    super();

    log.error("Book not found");
  }
}
