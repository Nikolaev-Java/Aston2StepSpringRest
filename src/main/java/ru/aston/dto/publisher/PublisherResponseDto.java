package ru.aston.dto.publisher;

import java.util.List;
import ru.aston.dto.book.BookResponseDto;

public class PublisherResponseDto {

    private long id;
    private String name;
    private String city;
    private List<BookResponseDto> books;

    public PublisherResponseDto() {
    }

    public PublisherResponseDto(long id, String name, String city, List<BookResponseDto> books) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.books = books;
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

    public List<BookResponseDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponseDto> books) {
        this.books = books;
    }
}
