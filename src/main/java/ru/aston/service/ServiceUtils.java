package ru.aston.service;

import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {

    public <E> void updateEntity(E param, Consumer<E> consumer) {
        if (Objects.nonNull(param)) {
            consumer.accept(param);
        }
    }
}
