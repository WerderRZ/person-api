package com.werdersoft.personapi.util;

import com.werdersoft.personapi.entity.BaseEntity;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    private Utils() {
        throw new RuntimeException("Класс не предназначен для инициализации!");
    }

    public static <T> T toValue(Optional<T> op, RuntimeException ex) {
        return op.orElseThrow(() -> ex);
    }

    public static <T extends BaseEntity> List<UUID> mapEntityToEntitiesToEntitiesIds(Set<T> entities) {
        return entities.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

}
