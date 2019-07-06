package com.github.marschall.jfr.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.ShardingKey;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;

final class JfrConnection implements Connection {

  private final Connection delegate;

  JfrConnection(Connection delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  private static JdbcOperationEvent newCreateStatementEvent() {
    return newConnectionEvent("createStatement");
  }

  private static JdbcOperationEvent newPrepareStatementEvent(String sql) {
    return newConnectionEvent("prepareStatement", sql);
  }

  private static JdbcOperationEvent newPrepareCallEvent(String sql) {
    return newConnectionEvent("prepareCall", sql);
  }
  
  private static JdbcOperationEvent newConnectionEvent(String operationName) {
    return newConnectionEvent(operationName, null);
  }

  private static JdbcOperationEvent newConnectionEvent(String operationName, String sql) {
    var event = new JdbcOperationEvent();
    event.operationObject = "Connection";
    event.operationName = operationName;
    event.query = sql;
    return event;
  }

  private static JdbcSavepointEvent newSafepointEvent(String operationName, JfrSavepoint savepoint) throws SQLException {
    var safepointEvent = new JdbcSavepointEvent();
    safepointEvent.operationName = operationName;
    if (savepoint instanceof JfrNamedSavepoint) {
      safepointEvent.savepointName = savepoint.getSavepointName();
    } else if (savepoint instanceof JfrUnNamedSavepoint) {
      safepointEvent.savepointId = savepoint.getSavepointId();
    }
    return safepointEvent;
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
  public Statement createStatement() throws SQLException {
    var event = newCreateStatementEvent();
    event.begin();
    try {
      var statement = this.delegate.createStatement();
      return new JfrStatement(this, statement);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    var event = newCreateStatementEvent();
    event.begin();
    try {
      var statement = this.delegate.createStatement(resultSetType, resultSetConcurrency);
      return new JfrStatement(this, statement);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    var event = newCreateStatementEvent();
    event.begin();
    try {
      var statement = this.delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
      return new JfrStatement(this, statement);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql, autoGeneratedKeys);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    event.begin();
    callEvent.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql, columnIndexes);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareStatementEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var preparedStatement = this.delegate.prepareStatement(sql, columnNames);
      return new JfrPreparedStatement(this, preparedStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareCallEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var callableStatement = this.delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      return new JfrCallableStatement(this, callableStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareCallEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var callableStatement = this.delegate.prepareCall(sql);
      return new JfrCallableStatement(this, callableStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    var callEvent = new JdbcCallEvent(sql);
    var event = newPrepareCallEvent(sql);

    callEvent.begin();
    event.begin();
    try {
      var callableStatement = this.delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
      return new JfrCallableStatement(this, callableStatement, callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    var event = newConnectionEvent("nativeSQL", sql);
    event.begin();
    try {
      return this.delegate.nativeSQL(sql);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    this.delegate.setAutoCommit(autoCommit);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return this.delegate.getAutoCommit();
  }

  @Override
  public void commit() throws SQLException {
    this.delegate.commit();
  }

  @Override
  public void rollback() throws SQLException {
    this.delegate.rollback();
  }

  @Override
  public void close() throws SQLException {
    this.delegate.close();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return this.delegate.isClosed();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    var event = newConnectionEvent("getMetaData");
    event.begin();
    try {
      return this.delegate.getMetaData();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    this.delegate.setReadOnly(readOnly);
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return this.delegate.isReadOnly();
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    this.delegate.setCatalog(catalog);
  }

  @Override
  public String getCatalog() throws SQLException {
    return this.delegate.getCatalog();
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    this.delegate.setTransactionIsolation(level);
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return this.delegate.getTransactionIsolation();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return this.delegate.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    this.delegate.clearWarnings();
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return this.delegate.getTypeMap();
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    this.delegate.setTypeMap(map);
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    this.delegate.setHoldability(holdability);
  }

  @Override
  public int getHoldability() throws SQLException {
    return this.delegate.getHoldability();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    var safepointEvent = new JdbcSavepointEvent();
    safepointEvent.operationName = "setSavepoint";
    safepointEvent.begin();
    Savepoint driverSavepoint;
    try {
      driverSavepoint = this.delegate.setSavepoint();
      safepointEvent.savepointId = driverSavepoint.getSavepointId();
    } finally {
      safepointEvent.end();
      safepointEvent.commit();
    }
    return new JfrUnNamedSavepoint(driverSavepoint);
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    var safepointEvent = new JdbcSavepointEvent();
    safepointEvent.savepointName = name;
    safepointEvent.operationName = "setSavepoint";
    safepointEvent.begin();
    try {
      return new JfrNamedSavepoint(this.delegate.setSavepoint(name));
    } finally {
      safepointEvent.end();
      safepointEvent.commit();
    }
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    if (savepoint instanceof JfrSavepoint) {
      var safepointEvent = newSafepointEvent("rollback", (JfrSavepoint) savepoint);

      safepointEvent.begin();
      try {
        this.delegate.rollback(((JfrSavepoint) savepoint).getDelegate());
      } finally {
        safepointEvent.end();
        safepointEvent.commit();
      }
    } else {
      this.delegate.rollback(savepoint);
    }
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    if (savepoint instanceof JfrSavepoint) {
      var safepointEvent = newSafepointEvent("releaseSavepoint", (JfrSavepoint) savepoint);

      safepointEvent.begin();
      try {
        this.delegate.releaseSavepoint(((JfrSavepoint) savepoint).getDelegate());
      } finally {
        safepointEvent.end();
        safepointEvent.commit();
      }
    } else {
      // there is no way to find out what kind of Safepoint we are and either of them may throw SQLException
      // so we simply do not generate an even tin this case
      this.delegate.releaseSavepoint(savepoint);
    }
  }

  @Override
  public Clob createClob() throws SQLException {
    return this.delegate.createClob();
  }

  @Override
  public Blob createBlob() throws SQLException {
    return this.delegate.createBlob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return this.delegate.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return this.delegate.createSQLXML();
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    var event = newConnectionEvent("isValid");
    event.begin();
    try {
      return this.delegate.isValid(timeout);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    this.delegate.setClientInfo(name, value);
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    this.delegate.setClientInfo(properties);
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    return this.delegate.getClientInfo(name);
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return this.delegate.getClientInfo();
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return this.delegate.createArrayOf(typeName, elements);
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return this.delegate.createStruct(typeName, attributes);
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    this.delegate.setSchema(schema);
  }

  @Override
  public String getSchema() throws SQLException {
    return this.delegate.getSchema();
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    this.delegate.abort(executor);
  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    this.delegate.setNetworkTimeout(executor, milliseconds);
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return this.delegate.getNetworkTimeout();
  }

  @Override
  public void beginRequest() throws SQLException {
    this.delegate.beginRequest();
  }

  @Override
  public void endRequest() throws SQLException {
    this.delegate.endRequest();
  }

  @Override
  public boolean setShardingKeyIfValid(ShardingKey shardingKey, ShardingKey superShardingKey, int timeout) throws SQLException {
    return this.delegate.setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
  }

  @Override
  public boolean setShardingKeyIfValid(ShardingKey shardingKey, int timeout) throws SQLException {
    return this.delegate.setShardingKeyIfValid(shardingKey, timeout);
  }

  @Override
  public void setShardingKey(ShardingKey shardingKey, ShardingKey superShardingKey) throws SQLException {
    this.delegate.setShardingKey(shardingKey, superShardingKey);
  }

  @Override
  public void setShardingKey(ShardingKey shardingKey) throws SQLException {
    this.delegate.setShardingKey(shardingKey);
  }

}
