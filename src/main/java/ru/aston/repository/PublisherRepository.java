package ru.aston.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @EntityGraph(attributePaths = "books")
    Optional<Publisher> findById(long id);
}
