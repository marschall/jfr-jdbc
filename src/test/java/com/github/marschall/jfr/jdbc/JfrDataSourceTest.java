package com.github.marschall.jfr.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class JfrDataSourceTest {

  private EmbeddedDatabase database;

  private JdbcOperations jdbcTemplate;

  @BeforeEach
  public void setUp() {
    this.database = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .build();
    this.jdbcTemplate = new JdbcTemplate(new JfrDataSource(this.database));
  }

  @AfterEach
  public void tearDown() {
    this.database.shutdown();
  }

  @Test
  void selectNoBindParameters() {
    Integer result = this.jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class);
    assertEquals(Integer.valueOf(1), result);
  }

  @Test
  void selectWithBindParameters() {
    Integer result = this.jdbcTemplate.queryForObject("SELECT 1 FROM dual WHERE 1 < ?", Integer.class, 2);
    assertEquals(Integer.valueOf(1), result);
  }
  
  @Test
  void clearParameters() {
    Integer computedSum = this.jdbcTemplate.execute(connection -> connection.prepareStatement("SELECT X FROM SYSTEM_RANGE(1, ?)"),
        (PreparedStatement preparedStatement) -> {
          preparedStatement.setInt(1, 3);
          int sum = 0;
          try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
              sum += resultSet.getInt(1);
            }
          }

          preparedStatement.clearParameters();

          preparedStatement.setInt(1, 4);
          try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
              sum += resultSet.getInt(1);
            }
          }

          return sum;
        });
    
    assertEquals(Integer.valueOf(16), computedSum);
  }

  @Test
  void callFunction() {
    Map<String, Object> result = this.jdbcTemplate.call(
        con -> con.prepareCall("{? = call MEMORY_USED()}"),
        Collections.emptyList());
    assertNotNull(result);
  }

}
