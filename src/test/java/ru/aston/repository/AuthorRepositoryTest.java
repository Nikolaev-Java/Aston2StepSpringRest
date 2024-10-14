package ru.aston.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.config.JpaConfig;
import ru.aston.dataUtils.CreatedData;
import ru.aston.entity.Author;
import ru.aston.entity.Book;
import ru.aston.entity.Publisher;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@DirtiesContext
@Transactional
class AuthorRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withInitScript("./schema.sql");
    @Autowired
    private AuthorRepository repository;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test find by id author functionality")
    void givenAuthorCrudRepository_whenFindAuthor_thenCorrect() {
        //given
        Author expected = CreatedData.createAuthor(1);
        Book book1 = CreatedData.createBook(1);
        Book book2 = CreatedData.createBook(2);
        Book book5 = CreatedData.createBook(5);
        Publisher publisher1 = CreatedData.createPublisher(1);
        Publisher publisher2 = CreatedData.createPublisher(2);
        Publisher publisher3 = CreatedData.createPublisher(3);
        Publisher publisher4 = CreatedData.createPublisher(4);
        Set<Publisher> publishersBook1 = Set.of(publisher1, publisher4, publisher2);
        book1.setPublishers(publishersBook1);
        Set<Publisher> publishersBook2 = Set.of(publisher3);
        book2.setPublishers(publishersBook2);
        Set<Publisher> publishersBook5 = Set.of(publisher3);
        book5.setPublishers(publishersBook5);
        Set<Book> authorsBook = Set.of(book1, book5, book2);
        expected.setBooks(authorsBook);
        //when
        Optional<Author> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
            .get()
            .isEqualTo(expected);
        assertThat(actual.get().getBooks()).hasSize(authorsBook.size()).hasSameElementsAs(authorsBook);
    }

    @Test
    @DisplayName("Test find by id author without books functionality")
    void givenAuthorCrudRepository_whenFindAuthorWithout_thenCorrect() {
        //given
        Author newAuthor = Author.builder().firstName("test").lastName("test").build();
        Author expected = repository.save(newAuthor);
        //when
        Optional<Author> actual = repository.findById(expected.getId());
        Author author = actual.get();
        //that
        assertThat(actual).isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .ignoringActualNullFields()
            .isEqualTo(expected);
        assertThat(actual.get().getBooks()).isNull();
    }

    @Test
    @DisplayName("Test find by id author not existing functionality")
    void givenAuthorCrudRepository_whenFindAuthorNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Author> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }
}