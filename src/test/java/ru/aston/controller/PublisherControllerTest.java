package ru.aston.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aston.matcher.ResponseBodyMatcher.responseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aston.dto.publisher.PublisherRequestDto;
import ru.aston.dto.publisher.PublisherResponseDto;
import ru.aston.dto.publisher.PublisherResponseDtoShort;
import ru.aston.exception.ErrorHandlingControllerAdvice;
import ru.aston.exception.NotFoundException;
import ru.aston.service.publisher.PublisherService;

class PublisherControllerTest {

    private MockMvc mvc;
    private PublisherService service = Mockito.mock(PublisherService.class);
    private static final String URL = "/publishers";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() throws Exception {
        this.mvc = MockMvcBuilders.standaloneSetup(new PublisherController(service))
            .setControllerAdvice(new ErrorHandlingControllerAdvice())
            .build();
    }

    @Test
    @DisplayName("Endpoint /publisher GET request functionality. Get all publisher")
    void givenPublisherServlet_whenDoGetAll_thenCorrect() throws Exception {
        //given
        PublisherResponseDtoShort dto = new PublisherResponseDtoShort();
        dto.setId(1L);
        dto.setName("test");
        dto.setCity("test");
        BDDMockito.given(service.getAllPublishers()).willReturn(List.of(dto));
        //when
        ResultActions result = mvc.perform(get(URL));
        //that
        result.andExpect(status().isOk())
            .andExpect(
                responseBody().containsListAsJson(List.of(dto), new TypeReference<List<PublisherResponseDtoShort>>() {
                }));
        verify(service, times(1)).getAllPublishers();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} GET request functionality. Get by id publisher")
    void givenPublisherServlet_whenDoGetById_thenCorrect() throws Exception {
        //given
        PublisherResponseDto responseDto = new PublisherResponseDto();
        responseDto.setId(1L);
        responseDto.setName("test");
        responseDto.setCity("test");
        responseDto.setBooks(new ArrayList<>());
        BDDMockito.given(service.getById(anyLong())).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(get(URL + "/1"));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, PublisherResponseDto.class));
        verify(service, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} GET request functionality. Get by id not exist publisher")
    void givenPublisherServlet_whenDoGetByIdNotExistPublisher_thenCorrect() throws Exception {
        //given
        BDDMockito.given(service.getById(anyLong())).willThrow(new NotFoundException("Publisher not found"));
        //when
        ResultActions result = mvc.perform(get(URL + "/1"));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Publisher not found"));
        verify(service, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /publisher POST request functionality. Create publisher")
    void givenPublisherServlet_whenDoPostCreatePublisher_thenCorrect() throws Exception {
        //given
        PublisherResponseDtoShort responseDto = new PublisherResponseDtoShort();
        responseDto.setId(1L);
        responseDto.setName("test");
        responseDto.setCity("test");
        PublisherRequestDto requestDto = new PublisherRequestDto();
        requestDto.setName("test");
        requestDto.setCity("test");
        String json = objectMapper.writeValueAsString(requestDto);
        BDDMockito.given(service.createPublisher(any(PublisherRequestDto.class))).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, PublisherResponseDtoShort.class));
        verify(service, times(1)).createPublisher(any(PublisherRequestDto.class));
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Update publisher")
    void givenPublisherServlet_whenDoPutUpdateById_thenException() throws Exception {
        //given
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        String json = objectMapper.writeValueAsString(dto);
        PublisherResponseDtoShort responseDto = new PublisherResponseDtoShort();
        responseDto.setId(1L);
        responseDto.setName("test");
        responseDto.setCity("test");
        BDDMockito.given(service.update(any(PublisherRequestDto.class), anyLong())).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, PublisherResponseDtoShort.class));
        verify(service, times(1)).update(any(PublisherRequestDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Update not exist publisher")
    void givenPublisherServlet_whenDoPutIncorrectId_thenException() throws Exception {
        //given
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(service.update(any(PublisherRequestDto.class), anyLong()))
            .willThrow(new NotFoundException("Publisher not found"));
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Publisher not found"));
        verify(service, times(1)).update(any(PublisherRequestDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} DELETE request functionality. Delete by id publisher")
    void givenPublisherServlet_whenDoDeleteById_thenCorrect() throws Exception {
        //when
        ResultActions result = mvc.perform(delete(URL + "/1"));
        //that
        result.andExpect(status().isOk());
        verify(service, times(1)).deleteById(anyLong());
    }
}