package ru.aston.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.aston.dataUtils.CreatedData;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.dto.mapping.AuthorMapper;
import ru.aston.dto.mapping.AuthorMapperImpl;
import ru.aston.dto.mapping.BookMapper;
import ru.aston.dto.mapping.BookMapperImpl;
import ru.aston.entity.Author;
import ru.aston.entity.Book;

class AuthorMapperTest {

    private AuthorMapper authorMapper = new AuthorMapperImpl();
    private BookMapper bookMapper = new BookMapperImpl();
    private Author author = CreatedData.createAuthor(1);
    private Book book = CreatedData.createBook(1);

    @Test
    void authorToResponseDto() {
        //given
        AuthorResponseDto expected = new AuthorResponseDto();
        expected.setId(author.getId());
        expected.setFirstName(author.getFirstName());
        expected.setLastName(author.getLastName());
        author.setBooks(Set.of(book));
        BookWithPublisherDto bookWithPublisherDto = bookMapper.toBookWithPublisherDto(book);
        expected.setBooks(List.of(bookWithPublisherDto));
        //when
        AuthorResponseDto actual = authorMapper.toResponseDto(author);
        AuthorResponseDto actualNull = authorMapper.toResponseDto(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void fromAuthorRequestDto() {
        //given
        Author expected = CreatedData.createAuthor(1);
        AuthorRequestDto authorRequestDto = new AuthorRequestDto();
        authorRequestDto.setFirstName(expected.getFirstName());
        authorRequestDto.setLastName(expected.getLastName());
        //when
        Author actual = authorMapper.fromAuthorRequestDto(authorRequestDto);
        actual.setId(author.getId());
        Author actualNull = authorMapper.fromAuthorRequestDto(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void authorToResponseDtoShort() {
        //given
        AuthorResponseDtoShort expected = new AuthorResponseDtoShort();
        expected.setId(author.getId());
        expected.setFirstName(author.getFirstName());
        expected.setLastName(author.getLastName());
        //when
        AuthorResponseDtoShort actual = authorMapper.toResponseDtoShort(author);
        AuthorResponseDtoShort actualNull = authorMapper.toResponseDtoShort(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void authorListToResponseDtoShortList() {
        //given
        Author author1 = CreatedData.createAuthor(2);
        AuthorResponseDtoShort dto1 = new AuthorResponseDtoShort();
        dto1.setId(author.getId());
        dto1.setFirstName(author.getFirstName());
        dto1.setLastName(author.getLastName());
        AuthorResponseDtoShort dto2 = new AuthorResponseDtoShort();
        dto2.setId(author1.getId());
        dto2.setFirstName(author1.getFirstName());
        dto2.setLastName(author1.getLastName());
        List<AuthorResponseDtoShort> expectedList = List.of(dto1, dto2);
        //when
        List<AuthorResponseDtoShort> actual = authorMapper.toResponseDtoShortList(List.of(author, author1));
        List<AuthorResponseDtoShort> actualNull = authorMapper.toResponseDtoShortList(null);
        //that
        assertThat(actual).isNotEmpty()
            .usingRecursiveComparison()
            .isEqualTo(expectedList);
        assertThat(actualNull).isNull();
    }
}