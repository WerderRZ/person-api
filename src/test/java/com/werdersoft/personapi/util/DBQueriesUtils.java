package com.werdersoft.personapi.util;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.EmployeeDTO;
import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.subdivision.Subdivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class DBQueriesUtils {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBQueriesUtils(@Lazy JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteAllRecords() {
        List<String> tableNames = List.of(
                "person",
                "company",
                "subdivision",
                "\"subdivisions-companies\"",
                "employee"
        );
        tableNames.forEach(tableName -> jdbcTemplate.execute("DELETE from " + tableName));
    }

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


    public List<Person> selectPersonsById(UUID id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        try {
            return jdbcTemplate.query(
                    sql,
                    ps -> ps.setObject(1, id),
                    (resultSet, i) -> Person.builder()
                            .id(resultSet.getObject(1, UUID.class))
                            .name(resultSet.getString(2))
                            .age(resultSet.getInt(3))
                            .build()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Company> selectCompaniesById(UUID id) {
        String sql = "SELECT * FROM company WHERE id = ?";
        try {
            return jdbcTemplate.query(
                    sql,
                    ps -> ps.setObject(1, id),
                    (resultSet, i) -> Company.builder()
                            .id(resultSet.getObject(1, UUID.class))
                            .name(resultSet.getString(2))
                            .region(Region.valueOf(resultSet.getString(3)))
                            .build()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subdivision> selectSubdivisionById(UUID id) {
        String sql = "SELECT id, name FROM subdivision WHERE id = ?";
        try {
            return jdbcTemplate.query(
                    sql,
                    ps -> ps.setObject(1, id),
                    (resultSet, i) -> Subdivision.builder()
                            .id(resultSet.getObject(1, UUID.class))
                            .name(resultSet.getString(2))
                            .build()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<UUID, UUID>> selectSubdivisionsCompaniesRowsById(UUID subdivisionId, UUID companyId) {
        String sql = "SELECT company_id, subdivision_id FROM \"subdivisions-companies\" WHERE "
                + "company_id = ? AND subdivision_id = ?";
        try {
            return jdbcTemplate.query(
                    sql,
                    ps -> {
                        ps.setObject(1, companyId);
                        ps.setObject(2, subdivisionId);
                    },
                    (resultSet, i) -> Map.of(
                            resultSet.getObject(1, UUID.class),
                            resultSet.getObject(2, UUID.class))

            );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmployeeDTO> selectEmployeeById(UUID id) {
        String sql = "SELECT id, position, salary, person_id, subdivision_id FROM employee WHERE id = ?";
        try {
            return jdbcTemplate.query(
                    sql,
                    ps -> ps.setObject(1, id),
                    (resultSet, i) -> EmployeeDTO.builder()
                            .id(resultSet.getObject(1, UUID.class))
                            .position(Position.valueOf(resultSet.getString(2)))
                            .salary(new BigDecimal(resultSet.getInt(3)))
                            .personId(resultSet.getObject(4, UUID.class))
                            .subdivisionId(resultSet.getObject(5, UUID.class))
                            .build()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
