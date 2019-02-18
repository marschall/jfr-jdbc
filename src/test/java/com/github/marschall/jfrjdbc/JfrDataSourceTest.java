package com.github.marschall.jfrjdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class JfrDataSourceTest {

  @Test
  void test() {
    EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.H2)
      .build();
    try {
      JdbcOperations jdbcTemplate = new JdbcTemplate(new JfrDataSource(database));
      Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class);
      assertEquals(Integer.valueOf(1), result);
    } finally {
      database.shutdown();
    }
  }

}
