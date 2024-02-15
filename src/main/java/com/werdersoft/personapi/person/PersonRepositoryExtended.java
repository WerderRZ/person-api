package com.werdersoft.personapi.person;

import com.werdersoft.personapi.util.BaseNativeExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Repository
public class PersonRepositoryExtended {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pas;

    public void savePersonsInBatches_EntityManager(List<Person> persons) {

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

        int savedRows = BaseNativeExecution.batchProcess(entityManager, persons, query, biConsumer);
        if (savedRows != persons.size()) {
            throw new RuntimeException("not saved in DB rows: " + (persons.size() - savedRows));
        }
    }

    public void savePersonsInBatches_JDBCTemplate(List<Person> persons) {

        String query = "INSERT INTO person (id, name, age, external_id, email) VALUES (?,?,?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Person person : persons) {
            Object[] values = new Object[] {
                    person.getId(),
                    person.getName(),
                    person.getAge(),
                    person.getExternalID(),
                    person.getEmail()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(query, batchArgs);

    }

    public void savePersonsInBatches_OneQuery(List<Person> persons) {

        try (Connection connection = DriverManager.getConnection(url, user, pas)) {

            Statement statement = connection.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO person (id, name, age, external_id, email) VALUES");

            for (Person person : persons) {
                sb.append(" (");
                sb.append("'").append(person.getId().toString()).append("', ");
                sb.append("'").append(person.getName()).append("', ");
                sb.append("NULL, ");
                sb.append("'").append(person.getExternalID()).append("', ");
                sb.append("'").append(person.getEmail()).append("'");
                sb.append("),");
            }
            sb.deleteCharAt(sb.length() - 1);

            String query = sb.toString();
            boolean result = statement.execute(query);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
