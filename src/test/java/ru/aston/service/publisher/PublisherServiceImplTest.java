package ru.aston.service.publisher;

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
import ru.aston.dto.mapping.PublisherMapperImpl;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.entity.Publisher;
import ru.aston.exception.NotFoundException;
import ru.aston.repository.PublisherRepository;
import ru.aston.service.ServiceUtils;


@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    private PublisherRepository repository = Mockito.mock(PublisherRepository.class);
    private final PublisherServiceImpl service = new PublisherServiceImpl(repository, new ServiceUtils(),
        new PublisherMapperImpl());

    @Test
    @DisplayName("Update author (full param) functionality")
    void givenAuthorService_whenUpdateAuthor_thenUpdateAuthor() {
        //given
        Publisher publisher = CreatedData.createPublisher(1);
        PublisherRequestDto requestDto = new PublisherRequestDto("test", "test");
        PublisherResponseDtoShort expectedResponseDto = new PublisherResponseDtoShort(1L, "test", "test");

        Publisher returnedRepository = Publisher.builder().id(1).name("test").city("test").build();
        BDDMockito.given(repository.findById(publisher.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(publisher)).willReturn(publisher);
        //when
        PublisherResponseDtoShort actual = service.update(requestDto, publisher.getId());
        //that
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedResponseDto);
        verify(repository, times(1)).save(publisher);
    }

    @Test
    @DisplayName("Update author not exist functionality")
    void givenAuthorService_whenUpdateAuthorNotExist_thenException() {
        //given
        Publisher publisher = CreatedData.createPublisher(1);
        PublisherRequestDto requestDto = new PublisherRequestDto("test", "test");
        BDDMockito.given(repository.findById(anyLong())).willThrow(NotFoundException.class);
        //when
        //that
        assertThrows(NotFoundException.class, () -> service.update(requestDto, publisher.getId()));
        verify(repository, times(0)).save(publisher);
    }

    @Test
    @DisplayName("Update author (one param) functionality")
    void givenAuthorService_whenUpdateAuthorOneParam_thenUpdateAuthor() {
        //given
        Publisher publisher = Publisher.builder().id(1).name("update").city("test").build();
        PublisherRequestDto requestDto = new PublisherRequestDto("update", "test");
        PublisherResponseDtoShort expectedResponseDto = new PublisherResponseDtoShort(1L, "update", "test");
        Publisher returnedRepository = Publisher.builder().id(1).name("test").city("test").build();
        BDDMockito.given(repository.findById(publisher.getId())).willReturn(Optional.of(returnedRepository));
        BDDMockito.given(repository.save(publisher)).willReturn(publisher);
        //when
        PublisherResponseDtoShort actual = service.update(requestDto, publisher.getId());
        //that
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expectedResponseDto);
        verify(repository, times(1)).save(publisher);
    }
}