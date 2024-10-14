package ru.aston.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.service.book.BookService;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookDto createBook(@RequestBody BookDto bookDto) {
        return bookService.create(bookDto);
    }

    @GetMapping
    public List<BookResponseDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookWithPublisherDto findBookById(@PathVariable long id) {
        return bookService.getById(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable long id, @RequestBody BookUpdateDto bookDto) {
        return bookService.update(bookDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
