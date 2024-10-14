package ru.aston.dto.mapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.entity.Book;
import ru.aston.entity.Publisher;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    Publisher fromRequestDtoToPublisher(PublisherRequestDto publisherRequestDto);

    PublisherResponseDto toPublisherResponseDto(Publisher publisher);

    PublisherResponseDtoShort toPublisherResponseDtoShort(Publisher publisher);

    List<PublisherResponseDtoShort> toPublisherResponseDtoShortList(List<Publisher> publishers);

    default BookResponseDto toBookWithPublisherDto(Book book) {
        return Mappers.getMapper(BookMapper.class).toBookResponseDto(book);
    }
}
