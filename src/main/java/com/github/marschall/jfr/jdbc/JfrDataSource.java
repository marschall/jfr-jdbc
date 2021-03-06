package com.github.marschall.jfr.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKeyBuilder;
import java.util.Objects;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * A data source that wraps an other one and generates Flight Recorder events.
 */
public final class JfrDataSource implements DataSource {

  private final DataSource delegate;

  /**
   * Constructs a new JFR data source.
   * 
   * @param delegate the actual data source to delegate to, not {@code null}
   */
  public JfrDataSource(DataSource delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return this.delegate.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return this.delegate.isWrapperFor(iface);
  }

  @Override
  public Connection getConnection() throws SQLException {
    var event = new JdbcOperationEvent();
    event.operationObject = "DataSource";
    event.operationName = "getConnection";
    try {
      var connection = this.delegate.getConnection();
      return new JfrConnection(connection);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    var event = new JdbcOperationEvent();
    event.operationObject = "DataSource";
    event.operationName = "getConnection";
    try {
      var connection = this.delegate.getConnection(username, password);
      return new JfrConnection(connection);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return this.delegate.getParentLogger();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return this.delegate.getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    this.delegate.setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    this.delegate.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return this.delegate.getLoginTimeout();
  }

  @Override
  public ConnectionBuilder createConnectionBuilder() throws SQLException {
    var connectionBuilder = this.delegate.createConnectionBuilder();
    return new JfrConnectionBuilder(connectionBuilder);
  }

  @Override
  public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
    return this.delegate.createShardingKeyBuilder();
  }

}
