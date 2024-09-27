package de.workshops.bookshelf.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookRestControllerMockitoTest {

  @InjectMocks
  private BookRestController bookRestController;

  @Mock
  private BookService bookService;

  @Test
  void getAllBooks() {
    List<Book> mockedBookList = new ArrayList<>();
    mockedBookList.add(new Book());
    Mockito.when(bookService.getAllBooks()).thenReturn(mockedBookList);

    assertNotNull(bookRestController.getAllBooks());
    assertEquals(1, bookRestController.getAllBooks().size());
  }
}
