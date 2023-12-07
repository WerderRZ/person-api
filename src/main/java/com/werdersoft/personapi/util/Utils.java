package com.werdersoft.personapi.util;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.subdivision.Subdivision;

import java.util.*;
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

    public static List<UUID> mapEntitiesToEntitiesIds(Set<? extends BaseEntity> entity)  {
        if (entity != null) {
            return entity.stream()
                    .map(BaseEntity::getId)
                    .toList();
        } else {
            return new ArrayList<UUID>();
        }
    }

    public static <T extends BaseEntity> UUID mapEntityToEntityId(T entity) {
        return (entity != null) ? entity.getId() : null;
    }

}
