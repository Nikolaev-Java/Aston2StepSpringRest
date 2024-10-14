package ru.aston.service.book;

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
import ru.aston.dataUtils.CreatedData;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.mapping.BookMapperImpl;
import ru.aston.entity.Book;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.BookRepository;
import ru.aston.service.ServiceUtils;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private BookRepository repository = Mockito.mock(BookRepository.class);
    private final BookServiceImpl service = new BookServiceImpl(repository, new BookMapperImpl(), new ServiceUtils());

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenBookService_whenUpdateBook_thenUpdateBook() {
        //given
        Book book = CreatedData.createBook(1);
        BookUpdateDto dto = new BookUpdateDto();
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setName("test");
        expected.setYear(123);
        Book returnedRepository = Book.builder().id(1L).name("test").year(123).build();
        BDDMockito.given(repository.findById(book.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(book)).willReturn(book);
        //when
        BookDto actual = service.update(dto, book.getId());
        //that
        assertThat(actual).usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .ignoringActualNullFields()
            .isEqualTo(expected);
        verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenBookService_whenUpdateBookNotExist_thenException() {
        //given
        Book book = CreatedData.createBook(1);
        BookUpdateDto dto = new BookUpdateDto();
        BDDMockito.given(repository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> service.update(dto, book.getId()));
        verify(repository, times(0)).save(book);
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenBookService_whenUpdateBookOneParam_thenUpdateBook() {
        //given
        Book book = Book.builder().id(1).name("update").author(CreatedData.createAuthor(1)).build();
        BookUpdateDto dto = new BookUpdateDto();
        dto.setName("update");
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setName("update");
        expected.setYear(123);
        Book returnedRepository = Book.builder().id(1L).name("test").year(123).build();
        BDDMockito.given(repository.findById(book.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(book)).willReturn(book);
        //when
        BookDto actual = service.update(dto, book.getId());
        //that
        assertThat(actual).usingRecursiveComparison()
            .ignoringActualNullFields()
            .ignoringExpectedNullFields()
            .isEqualTo(expected);
        verify(repository, times(1)).save(book);
    }
}