package ru.aston.dto.mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.entity.Author;
import ru.aston.entity.Book;
import ru.aston.entity.Publisher;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookWithPublisherDto toBookWithPublisherDto(Book book);

    BookResponseDto toBookResponseDto(Book book);

    List<BookResponseDto> toBookResponseDtoList(List<Book> books);

    @Mapping(target = "author", source = "author", qualifiedByName = "toAuthor")
    @Mapping(target = "publishers", source = "publishers", qualifiedByName = "toPublishers")
    Book fromBookDto(BookDto bookDto);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "publishers", source = "publishers", qualifiedByName = "tuPublisherId")
    BookDto toBookDto(Book book);

    @Mapping(target = "author", source = "author", qualifiedByName = "toAuthor")
    Book fromBookUpdateDto(BookUpdateDto dto);

    default List<PublisherResponseDtoShort> toPublisherDtoShortList(List<Publisher> publishers) {
        return Mappers.getMapper(PublisherMapper.class).toPublisherResponseDtoShortList(publishers);
    }

    @Named(value = "toAuthor")
    default Author toAuthorFromId(Long id) {
        if (id == null) {
            return null;
        }
        return Author.builder().id(id).build();
    }

    @Named(value = "toPublishers")
    default Set<Publisher> toPublishersFromIds(List<Long> ids) {
        return ids.stream().map(id -> Publisher.builder().id(id).build()).collect(Collectors.toSet());
    }

    @Named("tuPublisherId")
    default List<Long> toPublishersIdFromPublisher(Set<Publisher> publishers) {
        return publishers.stream().map(Publisher::getId).toList();
    }
}
