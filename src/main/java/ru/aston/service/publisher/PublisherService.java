package ru.aston.service.publisher;

import java.util.List;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;

public interface PublisherService {

    List<PublisherResponseDtoShort> getAllPublishers();

    PublisherResponseDto getById(long id);

    PublisherResponseDtoShort createPublisher(PublisherRequestDto dto);

    PublisherResponseDtoShort update(PublisherRequestDto dto, long id);

    void deleteById(long id);
}
