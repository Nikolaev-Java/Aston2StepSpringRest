package ru.aston.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String city;
    @ManyToMany(mappedBy = "publishers")
    private Set<Book> books = new HashSet<>();

    public Publisher() {
    }

    public Publisher(long id, String name, String city, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.city = city;
        if (books != null) {
            this.books = books;
        }
    }

    public static PublisherBuilder builder() {
        return new PublisherBuilder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Publisher publisher)) {
            return false;
        }

        return id == publisher.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public static class PublisherBuilder {

        private long id;
        private String name;
        private String city;
        private Set<Book> books;

        PublisherBuilder() {
        }

        public PublisherBuilder id(long id) {
            this.id = id;
            return this;
        }

        public PublisherBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PublisherBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PublisherBuilder books(Set<Book> books) {
            this.books = books;
            return this;
        }

        public Publisher build() {
            return new Publisher(this.id, this.name, this.city, this.books);
        }
    }

}
