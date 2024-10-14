package ru.aston.service.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.dto.mapping.AuthorMapperImpl;
import ru.aston.entity.Author;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.AuthorRepository;
import ru.aston.service.ServiceUtils;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    private AuthorRepository authorRepository = Mockito.mock(AuthorRepository.class);
    private final AuthorServiceImpl authorService = new AuthorServiceImpl(authorRepository, new AuthorMapperImpl(),
        new ServiceUtils());

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenAuthorService_whenUpdateAuthor_thenUpdateAuthor() {
        //given
        Author expected = Author.builder().id(1).firstName("update").lastName("update").build();
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("update", "update");
        AuthorResponseDtoShort expectedDto = new AuthorResponseDtoShort(1L, "update", "update");
        Author returnedRepository = Author.builder().id(1).firstName("test").lastName("test").build();
        BDDMockito.given(authorRepository.findById(expected.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(authorRepository.save(expected)).willReturn(expected);
        //when
        AuthorResponseDtoShort actual = authorService.update(authorRequestDto, expected.getId());
        //that
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedDto);
        verify(authorRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenAuthorService_whenUpdateAuthorNotExist_thenException() {
        //given
        Author author = Author.builder().id(1).firstName("update").lastName("update").build();
        AuthorRequestDto dto = new AuthorRequestDto("update", "update");
        BDDMockito.given(authorRepository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> authorService.update(dto, anyLong()));
        verify(authorRepository, times(0)).save(author);
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenAuthorService_whenUpdateAuthorOneParam_thenUpdateAuthor() {
        //given
        Author expected = Author.builder().id(1).firstName("update").lastName("test").build();
        AuthorRequestDto dto = new AuthorRequestDto("update", "update");
        AuthorResponseDtoShort expectedDto = new AuthorResponseDtoShort(1L, "update", "test");
        Author returnedRepository = Author.builder().id(1).firstName("test").lastName("test").build();
        BDDMockito.given(authorRepository.findById(expected.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(authorRepository.save(expected)).willReturn(expected);
        //when
        AuthorResponseDtoShort actual = authorService.update(dto, expected.getId());
        //that
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedDto);
        verify(authorRepository, times(1)).save(expected);
    }
}