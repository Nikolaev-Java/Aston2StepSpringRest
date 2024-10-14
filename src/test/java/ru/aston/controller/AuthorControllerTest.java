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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.aston.dataUtils.CreatedData;
import ru.aston.dto.author.AuthorRequestDto;
import ru.aston.dto.author.AuthorResponseDto;
import ru.aston.dto.author.AuthorResponseDtoShort;
import ru.aston.entity.Author;
import ru.aston.exception.ErrorHandlingControllerAdvice;
import ru.aston.exception.NotFoundException;
import ru.aston.service.author.AuthorService;

class AuthorControllerTest {

    private MockMvc mvc;
    private final AuthorService authorService = Mockito.mock(AuthorService.class);
    private static final String URL = "/authors";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(new AuthorController(authorService))
            .setControllerAdvice(new ErrorHandlingControllerAdvice())
            .build();
    }

    @Test
    @DisplayName("Endpoint /author GET request functionality. Get all authors")
    void givenAuthorServlet_whenDoGetAll_thenCorrect() throws Exception {
        //given
        AuthorResponseDtoShort dto = new AuthorResponseDtoShort(1, "test", "test");
        BDDMockito.given(authorService.getAll()).willReturn(List.of(dto));
        //when
        ResultActions result = mvc.perform(get(URL)
            .contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isOk())
            .andExpect(
                responseBody().containsListAsJson(List.of(dto), new TypeReference<List<AuthorResponseDtoShort>>() {
                }));
        verify(authorService, times(1)).getAll();
        verify(authorService, times(0)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /author/{id} GET request functionality. Get by id author")
    void givenAuthorServlet_whenDoGetById_thenCorrect() throws Exception {
        //given
        AuthorResponseDto dto = new AuthorResponseDto(1, "test", "test", null);
        BDDMockito.given(authorService.getById(anyLong())).willReturn(dto);
        //when
        ResultActions result = mvc.perform(get(URL + "/" + dto.getId())
            .contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(dto, AuthorResponseDto.class));
        verify(authorService, times(0)).getAll();
        verify(authorService, times(1)).getById(anyLong());

    }

    @Test
    @DisplayName("Endpoint /author/{id} GET request functionality. Get by id not exist author")
    void givenAuthorServlet_whenDoGetByIdNotExistAuthor_thenException() throws Exception {
        //given
        BDDMockito.given(authorService.getById(anyLong()))
            .willThrow(new NotFoundException("Author with id " + 1 + " not found"));
        //when
        ResultActions result = mvc.perform(get(URL + "/" + 1).contentType(MediaType.APPLICATION_JSON));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Author with id " + 1 + " not found"));
        verify(authorService, times(0)).getAll();
        verify(authorService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Endpoint /author POST request functionality. Create author")
    void givenAuthorServlet_whenDoPostCreateAuthor_thenCorrect() throws Exception {
        //given
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        AuthorResponseDtoShort responseDto = new AuthorResponseDtoShort(1, "name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(authorService.create(any(AuthorRequestDto.class))).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, AuthorResponseDtoShort.class));
        verify(authorService, times(1)).create(any(AuthorRequestDto.class));
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Update not exist author")
    void givenAuthorServlet_whenDoPutIncorrectId_thenException() throws Exception {
        //given
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(authorService.update(any(AuthorRequestDto.class), anyLong())).
            willThrow(new NotFoundException("Author not found"));
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isNotFound())
            .andExpect(responseBody().containsError("Author not found"));
        verify(authorService, times(1)).update(any(AuthorRequestDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Update author")
    void givenAuthorServlet_whenDoPutUpdateById_thenException() throws Exception {
        //given
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        AuthorResponseDtoShort responseDto = new AuthorResponseDtoShort(1, "name1", "lastName1");
        String json = objectMapper.writeValueAsString(dto);
        BDDMockito.given(authorService.update(any(AuthorRequestDto.class), anyLong())).willReturn(responseDto);
        //when
        ResultActions result = mvc.perform(put(URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
        //that
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(responseDto, AuthorResponseDtoShort.class));
        verify(authorService, times(1)).update(any(AuthorRequestDto.class), anyLong());
    }

    @Test
    @DisplayName("Endpoint /author/{id} DELETE request functionality. Delete by id author")
    void givenAuthorServlet_whenDoDeleteById_thenCorrect() throws Exception {
        //when
        ResultActions result = mvc.perform(delete(URL + "/1"));
        //that
        result.andExpect(status().isOk());
        verify(authorService, times(1)).deleteById(anyLong());
    }
}