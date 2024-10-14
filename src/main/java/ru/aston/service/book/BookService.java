package ru.aston.service.book;

import java.util.List;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.book.BookWithPublisherDto;

public interface BookService {

    List<BookResponseDto> getAllBooks();

    BookWithPublisherDto getById(long id);

    BookDto create(BookDto dto);

    BookDto update(BookUpdateDto dto, long id);

    void deleteById(long id);
}
