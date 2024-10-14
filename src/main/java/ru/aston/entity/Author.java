package ru.aston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Book> books;

    public Author() {
    }

    public Author(long id, String firstName, String lastName, Set<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        if (books != null) {
            this.books = books;
        }
    }

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author author)) {
            return false;
        }

        return id == author.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public static class AuthorBuilder {

        private long id;
        private String firstName;
        private String lastName;
        private Set<Book> books;

        AuthorBuilder() {
        }

        public AuthorBuilder id(long id) {
            this.id = id;
            return this;
        }

        public AuthorBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthorBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthorBuilder books(Set<Book> books) {
            this.books = books;
            return this;
        }

        public Author build() {
            return new Author(this.id, this.firstName, this.lastName, this.books);
        }
    }
}
