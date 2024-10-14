package ru.aston.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.aston.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
          SELECT a FROM Author AS a LEFT JOIN FETCH a.books as b LEFT JOIN FETCH b.publishers WHERE a.id=?1
        """)
    Optional<Author> findById(long id);
}
