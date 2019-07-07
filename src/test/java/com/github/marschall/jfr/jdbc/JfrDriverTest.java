package com.github.marschall.jfr.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JfrDriverTest {

  @BeforeAll
  static void loadDriver() {
    org.h2.Driver.load();
  }

  @Test
  void connect() throws SQLException {
    try (var connection = DriverManager.getConnection("jdbc:jfr:h2:mem:")) {
      DatabaseMetaData metaData = connection.getMetaData();
      assertFalse(metaData.isReadOnly());
    }
  }

  @Test
  void preparedStatement() throws SQLException {
    try (var connection = DriverManager.getConnection("jdbc:jfr:h2:mem:");
            var preparedStatement = connection.prepareStatement("SELECT 1");
            var resultSet = preparedStatement.executeQuery()) {
      ResultSetMetaData metaData = resultSet.getMetaData();
      assertEquals(1, metaData.getColumnCount());
      while (resultSet.next()) {
        assertEquals(1, resultSet.getInt(1));
      }
    }
  }

}
