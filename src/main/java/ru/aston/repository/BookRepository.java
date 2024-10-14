package ru.aston.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "publishers"})
    Optional<Book> findById(long id);

    @EntityGraph(attributePaths = "author")
    List<Book> findAll();
}
