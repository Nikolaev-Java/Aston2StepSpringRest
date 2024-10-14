package ru.aston.service.publisher;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aston.dto.mapping.PublisherMapper;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.entity.Publisher;
import ru.aston.exception.ErrorMessage;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.PublisherRepository;
import ru.aston.service.ServiceUtils;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final ServiceUtils serviceUtils;
    private final PublisherMapper publisherMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, ServiceUtils serviceUtils,
        PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.serviceUtils = serviceUtils;
        this.publisherMapper = publisherMapper;
    }

    @Override
    public List<PublisherResponseDtoShort> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        return publisherMapper.toPublisherResponseDtoShortList(
            publishers);
    }

    @Override
    public PublisherResponseDto getById(long id) {
        Publisher publisher = publisherRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("Publisher", id)));
        return publisherMapper.toPublisherResponseDto(publisher);
    }

    @Override
    public PublisherResponseDtoShort createPublisher(PublisherRequestDto dto) {
        Publisher publisher = publisherMapper.fromRequestDtoToPublisher(dto);
        Publisher saved = publisherRepository.save(publisher);
        return publisherMapper.toPublisherResponseDtoShort(saved);
    }

    @Override
    public PublisherResponseDtoShort update(PublisherRequestDto dto, long id) {
        Publisher publisherToUpdate = publisherRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NON_FOUND.formatMsg("Publisher", id)));
        serviceUtils.updateEntity(dto.getCity(), publisherToUpdate::setCity);
        serviceUtils.updateEntity(dto.getName(), publisherToUpdate::setName);
        publisherRepository.save(publisherToUpdate);
        return publisherMapper.toPublisherResponseDtoShort(publisherToUpdate);
    }

    @Override
    public void deleteById(long id) {
        publisherRepository.deleteById(id);
    }
}
