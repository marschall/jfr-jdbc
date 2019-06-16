package com.github.marschall.jfr.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public final class JfrDriver implements Driver {
  
  static {
    try {
      DriverManager.registerDriver(new JfrDriver());
    } catch (SQLException e) {
      throw new IllegalStateException("could not register driver", e);
    }
  }

  public JfrDriver() {
    super();
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    if (!this.acceptsURL(url)) {
      throw new SQLException("unvalid url: " + url);
    }
    return DriverManager.getConnection(url.substring(4), info);
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    if (url == null) {
      throw new SQLException();
    }
    return url.startsWith("jfr:");
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
    return 1;
  }

  @Override
  public boolean jdbcCompliant() {
    return true;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return Logger.getLogger("com.github.marschall.jfrjdbc");
  }

}
