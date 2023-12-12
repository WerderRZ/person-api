package com.werdersoft.personapi.util;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.subdivision.Subdivision;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public static <T extends BaseEntity> List<UUID> mapEntityToEntitiesToEntitiesIds(Set<T> entities) {
        return entities.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    public static <T> Stream<T> getStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
