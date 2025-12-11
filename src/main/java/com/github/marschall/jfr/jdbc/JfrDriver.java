package com.github.marschall.jfr.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A driver that wraps a different driver and generates JFR events.
 *
 * <h2>Usage</h2>
 * To use the driver simply prepend "jfr:" to the JDBC connection URL
 * for which you want to generate JFR events. For example if you use
 * "jdbc:jfr:h2:mem:" you will get a connection for "jdbc:h2:mem:"
 */
public final class JfrDriver implements Driver {

  static {
    try {
      DriverManager.registerDriver(new JfrDriver());
    } catch (SQLException e) {
      throw new IllegalStateException("could not register driver", e);
    }
  }

  /**
   * Default constructor, supposed to be called by JDBC, not user code.
   */
  public JfrDriver() {
    super();
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    if (!this.acceptsURL(url)) {
      throw new SQLException("invalid url: " + url);
    }
    String connectionString = "jdbc:" + url.substring(9);
    var event = new JdbcOperationEvent();
    event.operationObject = "Driver";
    event.operationName = "connect";
    event.query = connectionString;
    try {
      return new JfrConnection(DriverManager.getConnection(connectionString, info));
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    if (url == null) {
      throw new SQLException();
    }
    return url.startsWith("jdbc:jfr:");
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 0;
  }

  @Override
  public int getMinorVersion() {
    return 4;
  }

  @Override
  public boolean jdbcCompliant() {
    return true;
  }

  @Override
  public Logger getParentLogger() {
    // REVIEW inconsistent with JfrDataSource, but hard to keep consistent
    // as don't know what we will delegate to
    return Logger.getLogger("com.github.marschall.jfr.jdbc");
  }

}
