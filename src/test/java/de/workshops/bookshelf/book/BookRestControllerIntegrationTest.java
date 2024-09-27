package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRestController bookRestController;

    @LocalServerPort
    private int port;

    @TestConfiguration
    static class JacksonTestConfiguration {

        @Bean
        Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
            return builder -> builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    @Test
    void getAllBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Clean Code")))
                .andReturn();

        String jsonPayload = mvcResult.getResponse().getContentAsString();
        List<Book> books = objectMapper.readValue(jsonPayload, new TypeReference<>() {
        });

        assertEquals(3, books.size());
        assertEquals("Clean Code", books.get(1).getTitle());
    }

    @Test
    void testWithRestAssuredMockMvc() {
        RestAssuredMockMvc.standaloneSetup(bookRestController);
        RestAssuredMockMvc.
            given().
            log().all().
            when().
            get("/book").
            then().
            log().all().
            statusCode(200).
            body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void testWithRestAssured() {
        RestAssured.port = port;
        RestAssured.
            given().
            log().all().
            when().
            get("/book").
            then().
            log().all().
            statusCode(200).
            body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void createBook() throws Exception {
        String author = "Eric Evans";
        String title = "Domain-Driven Design: Tackling Complexity in the Heart of Software";
        String isbn = "978-0321125217";
        String description = "This is not a book about specific technologies. It offers readers a systematic approach to domain-driven design, presenting an extensive set of design best practices, experience-based techniques, and fundamental principles that facilitate the development of software projects facing complex domains.";

        Book expectedBook = new Book();
        expectedBook.setAuthor(author);
        expectedBook.setTitle(title);
        expectedBook.setIsbn(isbn);
        expectedBook.setDescription(description);

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/book")
                .content(
                    """
                    {
                        "isbn": "%s",
                        "title": "%s",
                        "author": "%s",
                        "description": "%s"
                    }
                    """
                        .formatted(
                            isbn,
                            title,
                            author,
                            description
                        )
                )
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String jsonPayload = mvcResult.getResponse().getContentAsString();

        Book book = objectMapper.readValue(jsonPayload, Book.class);
        assertEquals(expectedBook, book);
    }
}
