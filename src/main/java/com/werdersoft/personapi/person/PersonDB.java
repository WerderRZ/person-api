package com.werdersoft.personapi.person;

import com.werdersoft.personapi.util.BaseNativeQueryRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class PersonDB {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveBatchOfPersons(List<Person> persons) {

        String query = "INSERT INTO person (id, name, age, external_id, email) VALUES (?,?,?,?,?)";
        BiConsumer<PreparedStatement, Person> biConsumer = (preparedStatement, person) -> {
            try {
                preparedStatement.setObject(1, person.getId());
                preparedStatement.setString(2, person.getName());
                preparedStatement.setInt(3, person.getAge() == null ? 0 : person.getAge());
                preparedStatement.setInt(4, person.getExternalID());
                preparedStatement.setString(5, person.getEmail());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        int savedRows = BaseNativeQueryRepository.batchProcess(entityManager, persons, query, biConsumer);
        if (savedRows != persons.size()) {
            throw new RuntimeException("not saved in DB rows: " + (persons.size() - savedRows));
        }
    }




}
