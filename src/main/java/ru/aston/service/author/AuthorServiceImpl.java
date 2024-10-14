package ru.aston.service.author;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.dto.mapping.AuthorMapper;
import ru.aston.entity.Author;
import ru.aston.exception.ErrorMessage;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.AuthorRepository;
import ru.aston.service.ServiceUtils;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ServiceUtils serviceUtils;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, ServiceUtils serviceUtils) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.serviceUtils = serviceUtils;
    }

    @Override
    public AuthorResponseDto getById(long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("Author", id)));
        return authorMapper.toResponseDto(author);
    }

    @Override
    public List<AuthorResponseDtoShort> getAll() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toResponseDtoShortList(authors);
    }

    @Override
    public AuthorResponseDtoShort create(AuthorRequestDto dto) {
        Author author = authorMapper.fromAuthorRequestDto(dto);
        author = authorRepository.save(author);
        return authorMapper.toResponseDtoShort(author);
    }

    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorResponseDtoShort update(AuthorRequestDto dto, long id) {
        Author authorToUpdate = authorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("Author", id)));
        serviceUtils.updateEntity(dto.getFirstName(), authorToUpdate::setFirstName);
        serviceUtils.updateEntity(dto.getLastName(), authorToUpdate::setLastName);
        authorToUpdate = authorRepository.save(authorToUpdate);
        return authorMapper.toResponseDtoShort(authorToUpdate);
    }
}
