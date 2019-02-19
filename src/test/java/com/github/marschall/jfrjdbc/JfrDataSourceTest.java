package com.github.marschall.jfrjdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    this.jdbcTemplate = new JdbcTemplate(new JfrDataSource(database));
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

}
