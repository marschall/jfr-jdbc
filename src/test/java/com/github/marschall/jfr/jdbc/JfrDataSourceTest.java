package com.github.marschall.jfr.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
  void callFunction() {
    Map<String, Object> result = this.jdbcTemplate.call(
        con -> con.prepareCall("{? = call MEMORY_USED()}"),
        Collections.emptyList());
    assertNotNull(result);
  }

}
