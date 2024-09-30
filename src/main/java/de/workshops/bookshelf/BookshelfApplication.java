package de.workshops.bookshelf;

import de.workshops.bookshelf.config.BookshelfProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BookshelfApplication {

	private final BookshelfProperties bookshelfProperties;

	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);
	}

	@PostConstruct
	private void printBookshelfProperties() {
		log.info("Bookshelf properties: {}, {}", bookshelfProperties.getSomeNumber(), bookshelfProperties.getSomeText());
	}
}
