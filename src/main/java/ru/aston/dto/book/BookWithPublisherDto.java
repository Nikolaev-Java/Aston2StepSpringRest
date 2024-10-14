package ru.aston.dto.book;

import java.util.List;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.dto.publisher.PublisherResponseDtoShort;

public class BookWithPublisherDto extends BookResponseDto {

    private List<PublisherResponseDtoShort> publishers;

    public BookWithPublisherDto() {
    }

    public BookWithPublisherDto(long id, String name, int year, AuthorResponseDtoShort author,
        List<PublisherResponseDtoShort> publishers) {
        super(id, name, year, author);
        this.publishers = publishers;
    }

    public List<PublisherResponseDtoShort> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<PublisherResponseDtoShort> publishers) {
        this.publishers = publishers;
    }
}
