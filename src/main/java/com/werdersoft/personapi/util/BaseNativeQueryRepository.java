package com.werdersoft.personapi.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Slf4j
public class BaseNativeQueryRepository {
    private static final int BATCH_SIZE = 3;

    public static <T> Integer batchProcess(EntityManager entityManager, List<T> items,
                                           String query, BiConsumer<PreparedStatement, T> parameterSetter) {
        AtomicInteger sum = new AtomicInteger();
        try (Session session = entityManager.unwrap(Session.class)) {
            session.doWork(connection -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    int i = 0;
                    for (T item : items) {
                        try {
                            parameterSetter.accept(preparedStatement, item);
                            preparedStatement.addBatch();

                            i++;
                            if (i % BATCH_SIZE == 0) {
                                int[] executed = preparedStatement.executeBatch();
                                sum.addAndGet(Arrays.stream(executed).sum());
                            }
                        } catch (Exception e) {
                            log.error("An exception occurred when processing a batch insert item", e);
                        }
                    }
                    if (i % BATCH_SIZE != 0) {
                        int[] executed = preparedStatement.executeBatch();
                        sum.addAndGet(Arrays.stream(executed).sum());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    log.error("An exception occurred when working with Prepared Statement", e.getMessage());
                }
            });
        }
        return sum.get();
    }

}
