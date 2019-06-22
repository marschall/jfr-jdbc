package com.github.marschall.jfr.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Objects;

class JfrStatement implements Statement {

  private final Statement delegate;
  private final Connection parent;
  final long objectId;

  JfrStatement(Connection parent, Statement delegate) {
    Objects.requireNonNull(parent, "parent");
    Objects.requireNonNull(delegate, "delegate");
    this.parent = parent;
    this.delegate = delegate;
    this.objectId = ObjectIdGenerator.nextId();
  }

  private JdbcObjectEvent newObjectEvent(String operationName) {
    // TODO save last SQL
    return this.newObjectEvent(operationName, null);
  }

  private JdbcObjectEvent newObjectEvent(String operationName, String query) {
    var event = new JdbcObjectEvent();
    event.operationObject = "PreparedStatement";
    event.operationName = operationName;
    event.query = query;
    event.objectId = this.objectId;
    return event;
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
  public void close() throws SQLException {
    this.delegate.close();
  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    return this.delegate.getMaxFieldSize();
  }

  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    this.delegate.setMaxFieldSize(max);
  }

  @Override
  public int getMaxRows() throws SQLException {
    return this.delegate.getMaxRows();
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    this.delegate.setMaxRows(max);
  }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    this.delegate.setEscapeProcessing(enable);
  }

  @Override
  public int getQueryTimeout() throws SQLException {
    return this.delegate.getQueryTimeout();
  }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    this.delegate.setQueryTimeout(seconds);
  }

  @Override
  public void cancel() throws SQLException {
    this.delegate.cancel();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    var event = this.newObjectEvent("getWarnings");
    event.begin();
    try {
      return this.delegate.getWarnings();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void clearWarnings() throws SQLException {
    this.delegate.clearWarnings();
  }

  @Override
  public void setCursorName(String name) throws SQLException {
    this.delegate.setCursorName(name);
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return this.delegate.getResultSet();
  }

  @Override
  public int getUpdateCount() throws SQLException {
    return this.delegate.getUpdateCount();
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    var event = this.newObjectEvent("getMoreResults");
    event.begin();
    try {
      return this.delegate.getMoreResults();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {
    this.delegate.setFetchDirection(direction);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    return this.delegate.getFetchDirection();
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    this.delegate.setFetchSize(rows);
  }

  @Override
  public int getFetchSize() throws SQLException {
    return this.delegate.getFetchSize();
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    return this.delegate.getResultSetConcurrency();
  }

  @Override
  public int getResultSetType() throws SQLException {
    return this.delegate.getResultSetType();
  }

  @Override
  public void addBatch(String sql) throws SQLException {
    var event = this.newObjectEvent("addBatch", sql);
    event.begin();
    try {
      this.delegate.addBatch(sql);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void clearBatch() throws SQLException {
    this.delegate.clearBatch();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return this.parent;
  }

  @Override
  public boolean getMoreResults(int current) throws SQLException {
    var event = this.newObjectEvent("getMoreResults");
    event.begin();
    try {
      return this.delegate.getMoreResults(current);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    var objectEvent = this.newObjectEvent("getGeneratedKeys");

    objectEvent.begin();

    try {
      var resultSet = this.delegate.getGeneratedKeys();
      return new JfrResultSet(this, resultSet);
    } finally {
      objectEvent.end();
      objectEvent.commit();
    }
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    var callEvent = new JfrCallEvent(sql);
    var objectEvent = this.newObjectEvent("executeQuery", sql);

    callEvent.begin();
    objectEvent.begin();

    try {
      var resultSet = this.delegate.executeQuery(sql);
      return new JfrCallResultSet(this, resultSet, callEvent);
    } finally {
      objectEvent.end();
      objectEvent.commit();
    }
  }

  @Override
  public boolean execute(String sql) throws SQLException {
    var event = this.newObjectEvent("execute", sql);
    event.begin();
    try {
      return this.delegate.execute(sql);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int executeUpdate(String sql) throws SQLException {
    var event = this.newObjectEvent("executeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeUpdate(sql);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    var event = this.newObjectEvent("executeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeUpdate(sql, autoGeneratedKeys);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    var event = this.newObjectEvent("executeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeUpdate(sql, columnIndexes);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    var event = this.newObjectEvent("executeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeUpdate(sql, columnNames);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    var event = this.newObjectEvent("execute", sql);
    event.begin();
    try {
      return this.delegate.execute(sql, autoGeneratedKeys);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    var event = this.newObjectEvent("execute", sql);
    event.begin();
    try {
      return this.delegate.execute(sql, columnIndexes);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException {
    var event = this.newObjectEvent("execute", sql);
    event.begin();
    try {
      return this.delegate.execute(sql, columnNames);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int[] executeBatch() throws SQLException {
    return this.delegate.executeBatch();
  }

  @Override
  public long[] executeLargeBatch() throws SQLException {
    return this.delegate.executeLargeBatch();
  }

  @Override
  public long executeLargeUpdate(String sql) throws SQLException {
    var event = this.newObjectEvent("executeLargeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeLargeUpdate(sql);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    var event = this.newObjectEvent("executeLargeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeLargeUpdate(sql, autoGeneratedKeys);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
    var event = this.newObjectEvent("executeLargeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeLargeUpdate(sql, columnIndexes);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
    var event = this.newObjectEvent("executeLargeUpdate", sql);
    event.begin();
    try {
      return this.delegate.executeLargeUpdate(sql, columnNames);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    return this.delegate.getResultSetHoldability();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return this.delegate.isClosed();
  }

  @Override
  public void setPoolable(boolean poolable) throws SQLException {
    this.delegate.setPoolable(poolable);
  }

  @Override
  public boolean isPoolable() throws SQLException {
    return this.delegate.isPoolable();
  }

  @Override
  public void closeOnCompletion() throws SQLException {
    this.delegate.closeOnCompletion();
  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    return this.delegate.isCloseOnCompletion();
  }

  @Override
  public long getLargeUpdateCount() throws SQLException {
    return this.delegate.getLargeUpdateCount();
  }

  @Override
  public void setLargeMaxRows(long max) throws SQLException {
    this.delegate.setLargeMaxRows(max);
  }

  @Override
  public long getLargeMaxRows() throws SQLException {
    return this.delegate.getLargeMaxRows();
  }

  @Override
  public String enquoteLiteral(String val) throws SQLException {
    return this.delegate.enquoteLiteral(val);
  }

  @Override
  public String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
    return this.delegate.enquoteIdentifier(identifier, alwaysQuote);
  }

  @Override
  public boolean isSimpleIdentifier(String identifier) throws SQLException {
    return this.delegate.isSimpleIdentifier(identifier);
  }

  @Override
  public String enquoteNCharLiteral(String val) throws SQLException {
    return this.delegate.enquoteNCharLiteral(val);
  }


}
