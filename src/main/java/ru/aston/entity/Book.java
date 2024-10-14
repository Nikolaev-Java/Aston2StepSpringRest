package ru.aston.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
    private Integer year;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "books_publishers",
        joinColumns = @JoinColumn(name = "books_id"),
        inverseJoinColumns = @JoinColumn(name = "publisher_id"))
    private Set<Publisher> publishers = new HashSet<>();

    public Book() {
    }

    public Book(long id, String name, Author author, Integer year, Set<Publisher> publishers) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        if (publishers != null) {
            this.publishers = publishers;
        }
    }

    public static BookBuilder builder() {
        return new BookBuilder();
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public void addPublisher(Publisher publisher) {
        this.publishers.add(publisher);
        publisher.getBooks().add(this);
    }

    public void removePublisher(Publisher publisher) {
        this.publishers.remove(publisher);
        publisher.getBooks().remove(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book book)) {
            return false;
        }

        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public static class BookBuilder {

        private long id;
        private String name;
        private Author author;
        private Integer year;
        private Set<Publisher> publishers;

        BookBuilder() {
        }

        public BookBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BookBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BookBuilder author(Author author) {
            this.author = author;
            return this;
        }

        public BookBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public BookBuilder publishers(Set<Publisher> publishers) {
            this.publishers = publishers;
            return this;
        }

        public Book build() {
            return new Book(this.id, this.name, this.author, this.year, this.publishers);
        }
    }
}
