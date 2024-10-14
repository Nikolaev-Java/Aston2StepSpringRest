package ru.aston.matcher;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.aston.exception.ApiError;

public class ResponseBodyMatcher {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringActualNullFields()
                .isEqualTo(expectedObject);
        };
    }

    public <T> ResultMatcher containsListAsJson(Object expectedObject, TypeReference<List<T>> targetType) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            List<T> actualObject = objectMapper.readValue(json, targetType);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }

//    public ResultMatcher containsErrorValid(String expectedMessage) {
//        return result -> {
//            String json = result.getResponse().getContentAsString();
//            List<ErrorResponse> actualObject = objectMapper.readValue(json, new TypeReference<List<ErrorResponse>>() {
//            });
//            actualObject.forEach(errorResponse -> assertThat(errorResponse.getError()).isEqualTo(expectedMessage));
//        };
//    }

    public ResultMatcher containsError(String expectedMessage) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ApiError actualObject = objectMapper.readValue(json, ApiError.class);
            assertThat(actualObject.getMessage()).isEqualTo(expectedMessage);
        };
    }

    public static ResponseBodyMatcher responseBody() {
        return new ResponseBodyMatcher();
    }
}
