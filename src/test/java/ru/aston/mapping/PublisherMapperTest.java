package ru.aston.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.aston.dataUtils.CreatedData;
import ru.aston.dto.mapping.BookMapper;
import ru.aston.dto.mapping.BookMapperImpl;
import ru.aston.dto.mapping.PublisherMapper;
import ru.aston.dto.mapping.PublisherMapperImpl;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.entity.Book;
import ru.aston.entity.Publisher;


class PublisherMapperTest {

    private BookMapper bookMapper = new BookMapperImpl();
    private PublisherMapper publisherMapper = new PublisherMapperImpl();
    private Book book = CreatedData.createBook(1);
    private Publisher publisher = CreatedData.createPublisher(1);

    @Test
    void fromRequestDtoToPublisher() {
        //given
        PublisherRequestDto dto = new PublisherRequestDto();
        dto.setName(publisher.getName());
        dto.setCity(publisher.getCity());
        //when
        Publisher actual = publisherMapper.fromRequestDtoToPublisher(dto);
        actual.setId(1);
        Publisher actualNull = publisherMapper.fromRequestDtoToPublisher(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(publisher);
        assertThat(actualNull).isNull();
    }

    @Test
    void toPublisherResponseDto() {
        //given
        PublisherResponseDto expected = new PublisherResponseDto();
        expected.setId(publisher.getId());
        expected.setName(publisher.getName());
        expected.setCity(publisher.getCity());
        expected.setBooks(List.of(bookMapper.toBookResponseDto(book)));
        publisher.setBooks(Set.of(book));
        //when
        PublisherResponseDto actual = publisherMapper.toPublisherResponseDto(publisher);
        PublisherResponseDto actualNull = publisherMapper.toPublisherResponseDto(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void toPublisherResponseDtoShort() {
        //given
        PublisherResponseDtoShort expected = new PublisherResponseDtoShort();
        expected.setId(publisher.getId());
        expected.setName(publisher.getName());
        expected.setCity(publisher.getCity());
        //when
        PublisherResponseDtoShort actual = publisherMapper.toPublisherResponseDtoShort(publisher);
        PublisherResponseDtoShort actualNull = publisherMapper.toPublisherResponseDtoShort(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }

    @Test
    void toPublisherResponseDtoShortList() {
        //given
        PublisherResponseDtoShort dto1 = new PublisherResponseDtoShort();
        dto1.setId(publisher.getId());
        dto1.setName(publisher.getName());
        dto1.setCity(publisher.getCity());
        Publisher publisher1 = CreatedData.createPublisher(2);
        PublisherResponseDtoShort dto2 = new PublisherResponseDtoShort();
        dto2.setId(publisher1.getId());
        dto2.setName(publisher1.getName());
        dto2.setCity(publisher1.getCity());
        List<PublisherResponseDtoShort> expected = List.of(dto1, dto2);
        //when
        List<PublisherResponseDtoShort> actual = publisherMapper.
            toPublisherResponseDtoShortList(List.of(publisher, publisher1));
        List<PublisherResponseDtoShort> actualNull = publisherMapper.
            toPublisherResponseDtoShortList(null);
        //that
        assertThat(actual).isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expected);
        assertThat(actualNull).isNull();
    }
}