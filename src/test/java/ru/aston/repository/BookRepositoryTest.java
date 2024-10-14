package ru.aston.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
class BookRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withInitScript("./schema.sql");
    @Autowired
    private BookRepository repository;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "./data.sql");
    }

    @Test
    @DisplayName("Test find by id book functionality")
    void giveBookCrudRepository_whenFindBook_thenCorrect() {
        //given
        Author authorBook = CreatedData.createAuthor(1);
        Book expected = CreatedData.createBook(1);
        Publisher publisher1 = CreatedData.createPublisher(1);
        Publisher publisher4 = CreatedData.createPublisher(4);
        Publisher publisher2 = CreatedData.createPublisher(2);
        expected.setAuthor(authorBook);
        Set<Publisher> publishersBook1 = Set.of(publisher1, publisher4, publisher2);
        expected.setPublishers(publishersBook1);
        //when
        Optional<Book> actual = repository.findById(expected.getId());
        //that
        assertThat(actual).isPresent()
            .get()
            .isEqualTo(expected);
        assertThat(actual.get().getAuthor()).isEqualTo(authorBook);
        assertThat(actual.get().getPublishers()).hasSize(publishersBook1.size()).hasSameElementsAs(publishersBook1);
    }

    @Test
    @DisplayName("Test find by id book not existing functionality")
    void givenBookCrudRepository_whenFindBookNotFound_thenResultEmpty() {
        //given
        //when
        Optional<Book> actual = repository.findById(99L);
        //that
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Test find all book functionality")
    void givenBookCrudRepository_whenFindALlBook_thenCorrect() {
        //given
        Book book1 = CreatedData.createBook(1);
        Book book2 = CreatedData.createBook(2);
        Book book3 = CreatedData.createBook(3);
        Book book4 = CreatedData.createBook(4);
        Book book5 = CreatedData.createBook(5);
        List<Book> expected = List.of(book1, book2, book3, book4, book5);
        //when
        List<Book> actual = repository.findAll();
        //that
        assertThat(actual).isNotEmpty()
            .isEqualTo(expected);
    }
}