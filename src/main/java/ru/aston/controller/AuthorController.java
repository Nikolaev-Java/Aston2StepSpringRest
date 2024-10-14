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
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.service.author.AuthorService;

@RestController
@RequestMapping(path = "/authors")
public class AuthorController {

    private final AuthorService service;

    @Autowired
    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuthorResponseDtoShort> getAllAuthors() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable long id) {
        return service.getById(id);
    }

    @PostMapping
    public AuthorResponseDtoShort createAuthor(@RequestBody AuthorRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public AuthorResponseDtoShort updateAuthor(@PathVariable long id, @RequestBody AuthorRequestDto dto) {
        return service.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable long id) {
        service.deleteById(id);
    }
}
