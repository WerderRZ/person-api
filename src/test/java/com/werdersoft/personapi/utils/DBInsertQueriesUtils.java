package com.werdersoft.personapi.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

public class DBInsertQueriesUtils {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void addRecordPersonWithId(UUID id) {
        String sql = "INSERT INTO person(id, name, age) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "Sam");
            preparedStatement.setInt(3, 22);
        });
    }

    public void addRecordCompanyWithId(UUID id) {
        String sql = "INSERT INTO company(id, name, region) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "Werdersoft");
            preparedStatement.setString(3, "ASIA");
        });
    }

    public void addRecordSubdivisionWithId(UUID id) {
        String sql = "INSERT INTO subdivision (id, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "IT");
        });
    }

    public void addRecordSubdivisionCompanyWithIds(UUID subdivisionId, UUID companyId) {
        String sql = "INSERT INTO \"subdivisions-companies\" (subdivision_id, company_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, subdivisionId);
            preparedStatement.setObject(2, companyId);
        });
    }

    public void addRecordEmployeeWithId(UUID id, UUID personId, UUID subdivisionId) {
        String sql = "INSERT INTO employee (id, position, salary, person_id, subdivision_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "MANAGER");
            preparedStatement.setBigDecimal(3, new BigDecimal(300));
            preparedStatement.setObject(4, personId);
            preparedStatement.setObject(5, subdivisionId);
        });
    }
}
