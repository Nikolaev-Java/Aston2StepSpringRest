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
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.service.publisher.PublisherService;

@RestController
@RequestMapping(path = "/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public PublisherResponseDtoShort createPublisher(@RequestBody PublisherRequestDto dto) {
        return publisherService.createPublisher(dto);
    }

    @GetMapping("/{id}")
    public PublisherResponseDto getPublisherById(@PathVariable long id) {
        return publisherService.getById(id);
    }

    @GetMapping
    public List<PublisherResponseDtoShort> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @PutMapping("/{id}")
    public PublisherResponseDtoShort updatePublisher(@PathVariable long id, @RequestBody PublisherRequestDto dto) {
        return publisherService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable long id) {
        publisherService.deleteById(id);
    }
}
