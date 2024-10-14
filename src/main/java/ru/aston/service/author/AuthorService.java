package ru.aston.service.author;

import java.util.List;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;

public interface AuthorService {

    AuthorResponseDto getById(long id);

    List<AuthorResponseDtoShort> getAll();

    AuthorResponseDtoShort create(AuthorRequestDto dto);

    void deleteById(long id);

    AuthorResponseDtoShort update(AuthorRequestDto dto, long id);
}
