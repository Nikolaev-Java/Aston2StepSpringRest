package ru.aston.service.book;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.dto.mapping.BookMapper;
import ru.aston.entity.Book;
import ru.aston.exception.ErrorMessage;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.BookRepository;
import ru.aston.service.ServiceUtils;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ServiceUtils serviceUtils;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, ServiceUtils serviceUtils) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.serviceUtils = serviceUtils;
    }

    @Override
    public List<BookResponseDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toBookResponseDtoList(books);
    }

    @Override
    public BookWithPublisherDto getById(long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("book", id)));
        return bookMapper.toBookWithPublisherDto(book);
    }

    @Override
    public BookDto create(BookDto dto) {
        Book book = bookMapper.fromBookDto(dto);
        book = bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Override
    public BookDto update(BookUpdateDto dto, long id) {
        Book bookToUpdate = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("book", id)));
        Book book = bookMapper.fromBookUpdateDto(dto);
        book.setId(id);
        serviceUtils.updateEntity(book.getName(), bookToUpdate::setName);
        serviceUtils.updateEntity(book.getYear(), bookToUpdate::setYear);
        serviceUtils.updateEntity(book.getAuthor(), bookToUpdate::setAuthor);
        bookRepository.save(bookToUpdate);
        return bookMapper.toBookDto(bookToUpdate);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
