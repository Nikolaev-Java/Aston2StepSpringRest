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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@Transactional
@Testcontainers
@DirtiesContext
class PublisherRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withInitScript("./schema.sql");
    @Autowired
    private PublisherRepository repository;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test find by id publisher functionality")
    void givePublisherCrudRepository_whenFindPublisher_thenCorrect() {
        //given
        Author author1 = CreatedData.createAuthor(1);
        Author author2 = CreatedData.createAuthor(2);
        Book book1 = CreatedData.createBook(1);
        book1.setAuthor(author1);
        Book book3 = CreatedData.createBook(3);
        book3.setAuthor(author2);
        Set<Book> publishersBook = Set.of(book1, book3);
        Publisher expected = CreatedData.createPublisher(1);
        expected.setBooks(publishersBook);
        //when
        Optional<Publisher> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
            .get()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("Test find by id publisher not existing functionality")
    void givenPublisherCrudRepository_whenFindPublisherNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Publisher> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }
}