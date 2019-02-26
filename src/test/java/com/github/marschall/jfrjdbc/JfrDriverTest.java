package com.github.marschall.jfrjdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class JfrDriverTest {

  @Test
  void connect() throws SQLException {
    try (var connection = DriverManager.getConnection("jfr:jdbc:h2:mem:");
         var preparedStatement = connection.prepareStatement("SELECT 1");
         var resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        assertEquals(1, resultSet.getInt(1));
      }
    }
  }

}
