package com.werdersoft.personapi;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Utils {
    private Utils() {
        throw new RuntimeException("Класс не предназначен для инициализации!");
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public static <T> T toValue(Optional<T> op, RuntimeException ex) {
        return op.orElseThrow(() -> ex);
    }
}
