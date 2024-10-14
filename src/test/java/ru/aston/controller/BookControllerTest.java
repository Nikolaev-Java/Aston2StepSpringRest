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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aston.dataUtils.CreatedData;
import ru.aston.dto.book.BookDto;
import ru.aston.dto.book.BookResponseDto;
import ru.aston.dto.book.BookUpdateDto;
import ru.aston.dto.book.BookWithPublisherDto;
import ru.aston.entity.Book;
import ru.aston.exception.ErrorHandlingControllerAdvice;
import ru.aston.exception.NotFoundException;
import ru.aston.service.book.BookService;

class BookControllerTest {

    private MockMvc mvc;
    private BookService service = Mockito.mock(BookService.class);
    private static final String URL = "/books";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(new BookController(service))
            .setControllerAdvice(new ErrorHandlingControllerAdvice())
            .build();
    }

    @Test
    @DisplayName("Endpoint /book GET request functionality. Get all book")
    void givenBookServlet_whenDoGetAll_thenCorrect() throws Exception {
        //given
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(1);
        responseDto.setName("book1");
        responseDto.setYear(1);
        BDDMockito.given(service.getAllBooks()).willReturn(List.of(responseDto));
        //when
        ResultActions result = mvc.perform(get(URL)
            .contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsListAsJson(List.of(responseDto),
                new TypeReference<List<BookResponseDto>>() {
                }));
        verify(service, times(1)).getAllBooks();
    }

    @Test
    @DisplayName("Endpoint /book/{id} GET request functionality. Get by id book")
    void givenBookServlet_whenDoGetById_thenCorrect() throws Exception {
        //given
        Book book = CreatedData.createBook(1);
        BookWithPublisherDto responseDto = new BookWithPublisherDto();
        responseDto.setId(1);
        responseDto.setName("book1");
        responseDto.setYear(1);
        BDDMockito.given(service.getById(anyLong())).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(get(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, BookWithPublisherDto.class));
        verify(service, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /book/{id} GET request functionality. Get by id not exist book")
    void givenBookServlet_whenDoGetByIdNotExistBook_thenCorrect() throws Exception {
        //given
        BDDMockito.given(service.getById(anyLong())).willThrow(new NotFoundException("Book not found"));
        //when
        ResultActions result = mvc.perform(get(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Book not found"));
        verify(service, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /book POST request functionality. Create book")
    void givenBookServlet_whenDoPostCreateBook_thenCorrect() throws Exception {
        //given
        BookDto dto = new BookDto(1, "name1", 1, 1, List.of(1L));
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(service.create(any(BookDto.class))).willReturn(dto);
        //when
        ResultActions result = mvc.perform(post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(dto, BookDto.class));
        verify(service, times(1)).create(any(BookDto.class));
    }

    @Test
    @DisplayName("Endpoint /book POST request functionality. Create book with not exist Author")
    void givenBookServlet_whenDoPostCreateBook_thenException() throws Exception {
        //given
        BookDto dto = new BookDto(1, "name1", 1, 1, List.of(1L));
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(service.create(any(BookDto.class)))
            .willThrow(new DataIntegrityViolationException("Not author"));
        //when
        ResultActions result = mvc.perform(post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isConflict())
            .andExpect(responseBody().containsError("Not author"));
        verify(service, times(1)).create(any(BookDto.class));
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Update not exist book")
    void givenBookServlet_whenDoPutIncorrectId_thenException() throws Exception {
        //given
        BookUpdateDto dto = new BookUpdateDto("name1", 1L, 1);
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(service.update(any(BookUpdateDto.class), anyLong()))
            .willThrow(new NotFoundException("Not author"));
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Not author"));
        verify(service, times(1)).update(any(BookUpdateDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Update book")
    void givenBookServlet_whenDoPutUpdateById_thenException() throws Exception {
        //given
        BookUpdateDto dto = new BookUpdateDto("name1", 1L, 1);
        BookDto response = new BookDto(1, "name1", 1, 1, List.of(1L));
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(service.update(any(BookUpdateDto.class), anyLong()))
            .willReturn(response);
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(dto, BookDto.class));
        verify(service, times(1)).update(any(BookUpdateDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /book/{id} DELETE request functionality. Delete by id book")
    void givenBookServlet_whenDoDeleteById_thenCorrect() throws Exception {
        //when
        ResultActions result = mvc.perform(delete(URL + "/1"));
        //that
        result.andExpect(status().isOk());
        verify(service, times(1)).deleteById(anyLong());
    }
}