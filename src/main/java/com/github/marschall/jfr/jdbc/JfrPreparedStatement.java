package com.github.marschall.jfr.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

class JfrPreparedStatement extends JfrStatement implements PreparedStatement {

  private final PreparedStatement delegate;

  JdbcCallEvent callEvent;

  private boolean closed;

  JfrPreparedStatement(Connection parent, PreparedStatement delegate, JdbcCallEvent callEvent) {
    super(parent, delegate);
    Objects.requireNonNull(delegate, "delegate");
    Objects.requireNonNull(callEvent, "callEvent");
    this.callEvent = callEvent;
    this.delegate = delegate;
    this.closed = false;
  }

  private JdbcObjectEvent newObjectEvent(String operationName) {
    var event = new JdbcObjectEvent();
    event.operationObject = "PreparedStatement";
    event.operationName = operationName;
    event.query = this.callEvent.query;
    event.objectId = this.objectId;
    return event;
  }

  @Override
  public void close() throws SQLException {
    if (!this.closed && !this.callEvent.closed) {
      this.callEvent.end();
      this.callEvent.commit();
      this.callEvent.closed = true;
      this.closed = true;
    }
    this.delegate.close();
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    var event = this.newObjectEvent("executeQuery");
    event.begin();

    try {
      return new JfrCallResultSet(this, this.delegate.executeQuery(), this.callEvent);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    var event = this.newObjectEvent("executeUpdate");
    event.begin();

    try {
      return this.delegate.executeUpdate();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean execute() throws SQLException {
    var event = this.newObjectEvent("execute");
    event.begin();

    try {
      return this.delegate.execute();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public long executeLargeUpdate() throws SQLException {
    var event = this.newObjectEvent("executeLargeUpdate");
    event.begin();

    try {
      return this.delegate.executeLargeUpdate();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public int[] executeBatch() throws SQLException {
    var event = this.newObjectEvent("executeBatch");
    event.begin();

    try {
      return this.delegate.executeBatch();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public long[] executeLargeBatch() throws SQLException {
    var event = this.newObjectEvent("executeLargeBatch");
    event.begin();

    try {
      return this.delegate.executeLargeBatch();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    this.delegate.setNull(parameterIndex, sqlType);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    this.delegate.setBoolean(parameterIndex, x);
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    this.delegate.setByte(parameterIndex, x);
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    this.delegate.setShort(parameterIndex, x);
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    this.delegate.setInt(parameterIndex, x);
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    this.delegate.setLong(parameterIndex, x);
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    this.delegate.setFloat(parameterIndex, x);
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    this.delegate.setDouble(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    this.delegate.setBigDecimal(parameterIndex, x);
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    this.delegate.setString(parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    this.delegate.setBytes(parameterIndex, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    this.delegate.setDate(parameterIndex, x);
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    this.delegate.setTime(parameterIndex, x);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    this.delegate.setTimestamp(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.delegate.setAsciiStream(parameterIndex, x, length);
  }

  @Override
  @Deprecated
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.delegate.setUnicodeStream(parameterIndex, x, length);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.delegate.setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void clearParameters() throws SQLException {
    if (!this.closed && !this.callEvent.closed) {
      this.callEvent.end();
      this.callEvent.commit();
      this.callEvent.closed = true;
      
      this.callEvent = new JdbcCallEvent(this.callEvent.query);
    }
    this.delegate.clearParameters();
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    this.delegate.setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    this.delegate.setObject(parameterIndex, x);
  }

  @Override
  public void addBatch() throws SQLException {
    this.delegate.addBatch();
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    this.delegate.setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    this.delegate.setRef(parameterIndex, x);
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    this.delegate.setBlob(parameterIndex, x);
  }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    this.delegate.setClob(parameterIndex, x);
  }

  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException {
    this.delegate.setArray(parameterIndex, x);
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    var event = new JdbcObjectEvent();
    event.operationObject = "PreparedStatement";
    event.operationName = "getMetaData";
    event.begin();
    try {
      return this.delegate.getMetaData();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    this.delegate.setDate(parameterIndex, x, cal);
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    this.delegate.setTime(parameterIndex, x, cal);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    this.delegate.setTimestamp(parameterIndex, x, cal);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    this.delegate.setNull(parameterIndex, sqlType, typeName);
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    this.delegate.setURL(parameterIndex, x);
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    var event = this.newObjectEvent("getParameterMetaData");
    event.begin();
    try {
      return this.delegate.getParameterMetaData();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    this.delegate.setRowId(parameterIndex, x);
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    this.delegate.setNString(parameterIndex, value);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    this.delegate.setNCharacterStream(parameterIndex, value, length);
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    this.delegate.setNClob(parameterIndex, value);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.delegate.setClob(parameterIndex, reader, length);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    this.delegate.setBlob(parameterIndex, inputStream, length);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.delegate.setNClob(parameterIndex, reader, length);
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    this.delegate.setSQLXML(parameterIndex, xmlObject);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    this.delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.delegate.setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.delegate.setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    this.delegate.setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    this.delegate.setAsciiStream(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    this.delegate.setBinaryStream(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    this.delegate.setCharacterStream(parameterIndex, reader);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    this.delegate.setNCharacterStream(parameterIndex, value);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    this.delegate.setClob(parameterIndex, reader);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    this.delegate.setBlob(parameterIndex, inputStream);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    this.delegate.setNClob(parameterIndex, reader);
  }

  @Override
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
    this.delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
    this.delegate.setObject(parameterIndex, x, targetSqlType);
  }

}
