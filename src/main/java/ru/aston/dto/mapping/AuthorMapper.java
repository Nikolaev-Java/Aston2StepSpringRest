package ru.aston.dto.mapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.entity.Author;
import ru.aston.entity.Book;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponseDto toResponseDto(Author author);

    Author fromAuthorRequestDto(AuthorRequestDto dto);

    AuthorResponseDtoShort toResponseDtoShort(Author author);

    List<AuthorResponseDtoShort> toResponseDtoShortList(List<Author> authorList);

    default BookWithPublisherDto toBookWithPublisherDto(Book book) {
        return Mappers.getMapper(BookMapper.class).toBookWithPublisherDto(book);
    }
}
