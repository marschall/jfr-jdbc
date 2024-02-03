package com.github.marschall.jfr.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

class JfrResultSet implements ResultSet {

  final ResultSet delegate;
  private final Statement parent;
  private final long objectId;
  private boolean closed;
  private JdbcCallEvent callEvent;
  private long rowCount;


  JfrResultSet(Statement parent, ResultSet delegate, JdbcCallEvent callEvent) {
    this.parent = parent;
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
    this.objectId = ObjectIdGenerator.nextId();
    this.callEvent = callEvent;
    this.closed = false;
  }

  private JdbcOperationEvent newOperationEvent(String operationName) {
    var event = new JdbcOperationEvent();
    event.operationObject = "ResultSet";
    event.operationName = operationName;
    event.objectId = this.objectId;
    return event;
  }

  @Override
  public void close() throws SQLException {
    if (!this.closed) {
      this.callEvent.rowCount = this.rowCount;
      this.callEvent.close();
      this.callEvent = null;
      this.closed = true;
    }
    this.delegate.close();
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
  public boolean next() throws SQLException {
    var event = this.newOperationEvent("next");
    event.begin();
    try {
      boolean next = this.delegate.next();
      if (next) {
        this.rowCount += 1;
      }
      return next;
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean wasNull() throws SQLException {
    return this.delegate.wasNull();
  }

  @Override
  public String getString(int columnIndex) throws SQLException {
    return this.delegate.getString(columnIndex);
  }

  @Override
  public boolean getBoolean(int columnIndex) throws SQLException {
    return this.delegate.getBoolean(columnIndex);
  }

  @Override
  public byte getByte(int columnIndex) throws SQLException {
    return this.delegate.getByte(columnIndex);
  }

  @Override
  public short getShort(int columnIndex) throws SQLException {
    return this.delegate.getShort(columnIndex);
  }

  @Override
  public int getInt(int columnIndex) throws SQLException {
    return this.delegate.getInt(columnIndex);
  }

  @Override
  public long getLong(int columnIndex) throws SQLException {
    return this.delegate.getLong(columnIndex);
  }

  @Override
  public float getFloat(int columnIndex) throws SQLException {
    return this.delegate.getFloat(columnIndex);
  }

  @Override
  public double getDouble(int columnIndex) throws SQLException {
    return this.delegate.getDouble(columnIndex);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
    return this.delegate.getBigDecimal(columnIndex, scale);
  }

  @Override
  public byte[] getBytes(int columnIndex) throws SQLException {
    return this.delegate.getBytes(columnIndex);
  }

  @Override
  public Date getDate(int columnIndex) throws SQLException {
    return this.delegate.getDate(columnIndex);
  }

  @Override
  public Time getTime(int columnIndex) throws SQLException {
    return this.delegate.getTime(columnIndex);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return this.delegate.getTimestamp(columnIndex);
  }

  @Override
  public InputStream getAsciiStream(int columnIndex) throws SQLException {
    return this.delegate.getAsciiStream(columnIndex);
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(int columnIndex) throws SQLException {
    return this.delegate.getUnicodeStream(columnIndex);
  }

  @Override
  public InputStream getBinaryStream(int columnIndex) throws SQLException {
    return this.delegate.getBinaryStream(columnIndex);
  }

  @Override
  public String getString(String columnLabel) throws SQLException {
    return this.delegate.getString(columnLabel);
  }

  @Override
  public boolean getBoolean(String columnLabel) throws SQLException {
    return this.delegate.getBoolean(columnLabel);
  }

  @Override
  public byte getByte(String columnLabel) throws SQLException {
    return this.delegate.getByte(columnLabel);
  }

  @Override
  public short getShort(String columnLabel) throws SQLException {
    return this.delegate.getShort(columnLabel);
  }

  @Override
  public int getInt(String columnLabel) throws SQLException {
    return this.delegate.getInt(columnLabel);
  }

  @Override
  public long getLong(String columnLabel) throws SQLException {
    return this.delegate.getLong(columnLabel);
  }

  @Override
  public float getFloat(String columnLabel) throws SQLException {
    return this.delegate.getFloat(columnLabel);
  }

  @Override
  public double getDouble(String columnLabel) throws SQLException {
    return this.delegate.getDouble(columnLabel);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
    return this.delegate.getBigDecimal(columnLabel, scale);
  }

  @Override
  public byte[] getBytes(String columnLabel) throws SQLException {
    return this.delegate.getBytes(columnLabel);
  }

  @Override
  public Date getDate(String columnLabel) throws SQLException {
    return this.delegate.getDate(columnLabel);
  }

  @Override
  public Time getTime(String columnLabel) throws SQLException {
    return this.delegate.getTime(columnLabel);
  }

  @Override
  public Timestamp getTimestamp(String columnLabel) throws SQLException {
    return this.delegate.getTimestamp(columnLabel);
  }

  @Override
  public InputStream getAsciiStream(String columnLabel) throws SQLException {
    return this.delegate.getAsciiStream(columnLabel);
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(String columnLabel) throws SQLException {
    return this.delegate.getUnicodeStream(columnLabel);
  }

  @Override
  public InputStream getBinaryStream(String columnLabel) throws SQLException {
    return this.delegate.getBinaryStream(columnLabel);
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
  public String getCursorName() throws SQLException {
    return this.delegate.getCursorName();
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    var event = this.newOperationEvent("getMetaData");
    event.begin();
    try {
      return this.delegate.getMetaData();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public Object getObject(int columnIndex) throws SQLException {
    return this.delegate.getObject(columnIndex);
  }

  @Override
  public Object getObject(String columnLabel) throws SQLException {
    return this.delegate.getObject(columnLabel);
  }

  @Override
  public int findColumn(String columnLabel) throws SQLException {
    return this.delegate.findColumn(columnLabel);
  }

  @Override
  public Reader getCharacterStream(int columnIndex) throws SQLException {
    return this.delegate.getCharacterStream(columnIndex);
  }

  @Override
  public Reader getCharacterStream(String columnLabel) throws SQLException {
    return this.delegate.getCharacterStream(columnLabel);
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    return this.delegate.getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
    return this.delegate.getBigDecimal(columnLabel);
  }

  @Override
  public boolean isBeforeFirst() throws SQLException {
    return this.delegate.isBeforeFirst();
  }

  @Override
  public boolean isAfterLast() throws SQLException {
    return this.delegate.isAfterLast();
  }

  @Override
  public boolean isFirst() throws SQLException {
    return this.delegate.isFirst();
  }

  @Override
  public boolean isLast() throws SQLException {
    return this.delegate.isLast();
  }

  @Override
  public void beforeFirst() throws SQLException {
    this.delegate.beforeFirst();
  }

  @Override
  public void afterLast() throws SQLException {
    this.delegate.afterLast();
  }

  @Override
  public boolean first() throws SQLException {
    this.rowCount = 0;
    return this.delegate.first();
  }

  @Override
  public boolean last() throws SQLException {
    this.rowCount = 0;
    return this.delegate.last();
  }

  @Override
  public int getRow() throws SQLException {
    return this.delegate.getRow();
  }

  @Override
  public boolean absolute(int row) throws SQLException {
    var event = this.newOperationEvent("absolute");
    event.begin();
    try {
      boolean moved = this.delegate.absolute(row);
      if (moved && row >= 1) {
        this.rowCount += 1L;
      }
      return moved;
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean relative(int rows) throws SQLException {
    var event = this.newOperationEvent("relative");
    event.begin();
    try {
      return this.delegate.relative(rows);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public boolean previous() throws SQLException {
    return this.delegate.previous();
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
  public int getType() throws SQLException {
    return this.delegate.getType();
  }

  @Override
  public int getConcurrency() throws SQLException {
    return this.delegate.getConcurrency();
  }

  @Override
  public boolean rowUpdated() throws SQLException {
    return this.delegate.rowUpdated();
  }

  @Override
  public boolean rowInserted() throws SQLException {
    return this.delegate.rowInserted();
  }

  @Override
  public boolean rowDeleted() throws SQLException {
    return this.delegate.rowDeleted();
  }

  @Override
  public void updateNull(int columnIndex) throws SQLException {
    this.delegate.updateNull(columnIndex);
  }

  @Override
  public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    this.delegate.updateBoolean(columnIndex, x);
  }

  @Override
  public void updateByte(int columnIndex, byte x) throws SQLException {
    this.delegate.updateByte(columnIndex, x);
  }

  @Override
  public void updateShort(int columnIndex, short x) throws SQLException {
    this.delegate.updateShort(columnIndex, x);
  }

  @Override
  public void updateInt(int columnIndex, int x) throws SQLException {
    this.delegate.updateInt(columnIndex, x);
  }

  @Override
  public void updateLong(int columnIndex, long x) throws SQLException {
    this.delegate.updateLong(columnIndex, x);
  }

  @Override
  public void updateFloat(int columnIndex, float x) throws SQLException {
    this.delegate.updateFloat(columnIndex, x);
  }

  @Override
  public void updateDouble(int columnIndex, double x) throws SQLException {
    this.delegate.updateDouble(columnIndex, x);
  }

  @Override
  public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
    this.delegate.updateBigDecimal(columnIndex, x);
  }

  @Override
  public void updateString(int columnIndex, String x) throws SQLException {
    this.delegate.updateString(columnIndex, x);
  }

  @Override
  public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    this.delegate.updateBytes(columnIndex, x);
  }

  @Override
  public void updateDate(int columnIndex, Date x) throws SQLException {
    this.delegate.updateDate(columnIndex, x);
  }

  @Override
  public void updateTime(int columnIndex, Time x) throws SQLException {
    this.delegate.updateTime(columnIndex, x);
  }

  @Override
  public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
    this.delegate.updateTimestamp(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
    this.delegate.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
    this.delegate.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
    this.delegate.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
    this.delegate.updateObject(columnIndex, x, scaleOrLength);
  }

  @Override
  public void updateObject(int columnIndex, Object x) throws SQLException {
    this.delegate.updateObject(columnIndex, x);
  }

  @Override
  public void updateNull(String columnLabel) throws SQLException {
    this.delegate.updateNull(columnLabel);
  }

  @Override
  public void updateBoolean(String columnLabel, boolean x) throws SQLException {
    this.delegate.updateBoolean(columnLabel, x);
  }

  @Override
  public void updateByte(String columnLabel, byte x) throws SQLException {
    this.delegate.updateByte(columnLabel, x);
  }

  @Override
  public void updateShort(String columnLabel, short x) throws SQLException {
    this.delegate.updateShort(columnLabel, x);
  }

  @Override
  public void updateInt(String columnLabel, int x) throws SQLException {
    this.delegate.updateInt(columnLabel, x);
  }

  @Override
  public void updateLong(String columnLabel, long x) throws SQLException {
    this.delegate.updateLong(columnLabel, x);
  }

  @Override
  public void updateFloat(String columnLabel, float x) throws SQLException {
    this.delegate.updateFloat(columnLabel, x);
  }

  @Override
  public void updateDouble(String columnLabel, double x) throws SQLException {
    this.delegate.updateDouble(columnLabel, x);
  }

  @Override
  public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
    this.delegate.updateBigDecimal(columnLabel, x);
  }

  @Override
  public void updateString(String columnLabel, String x) throws SQLException {
    this.delegate.updateString(columnLabel, x);
  }

  @Override
  public void updateBytes(String columnLabel, byte[] x) throws SQLException {
    this.delegate.updateBytes(columnLabel, x);
  }

  @Override
  public void updateDate(String columnLabel, Date x) throws SQLException {
    this.delegate.updateDate(columnLabel, x);
  }

  @Override
  public void updateTime(String columnLabel, Time x) throws SQLException {
    this.delegate.updateTime(columnLabel, x);
  }

  @Override
  public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
    this.delegate.updateTimestamp(columnLabel, x);
  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
    this.delegate.updateAsciiStream(columnLabel, x, length);
  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
    this.delegate.updateBinaryStream(columnLabel, x, length);
  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
    this.delegate.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
    this.delegate.updateObject(columnLabel, x, scaleOrLength);
  }

  @Override
  public void updateObject(String columnLabel, Object x) throws SQLException {
    this.delegate.updateObject(columnLabel, x);
  }

  @Override
  public void insertRow() throws SQLException {
    this.delegate.insertRow();
  }

  @Override
  public void updateRow() throws SQLException {
    this.delegate.updateRow();
  }

  @Override
  public void deleteRow() throws SQLException {
    this.delegate.deleteRow();
  }

  @Override
  public void refreshRow() throws SQLException {
    this.delegate.refreshRow();
  }

  @Override
  public void cancelRowUpdates() throws SQLException {
    this.delegate.cancelRowUpdates();
  }

  @Override
  public void moveToInsertRow() throws SQLException {
    this.delegate.moveToInsertRow();
  }

  @Override
  public void moveToCurrentRow() throws SQLException {
    this.delegate.moveToCurrentRow();
  }

  @Override
  public Statement getStatement() throws SQLException {
    Statement statement = this.delegate.getStatement();
    if (statement == null) {
      // allowed for DatabaseMetaData
      return null;
    } else {
      // REVIEW may not be correct for DatabaseMetaData
      return this.parent;
    }
  }

  @Override
  public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
    return this.delegate.getObject(columnIndex, map);
  }

  @Override
  public Ref getRef(int columnIndex) throws SQLException {
    return this.delegate.getRef(columnIndex);
  }

  @Override
  public Blob getBlob(int columnIndex) throws SQLException {
    return this.delegate.getBlob(columnIndex);
  }

  @Override
  public Clob getClob(int columnIndex) throws SQLException {
    return this.delegate.getClob(columnIndex);
  }

  @Override
  public Array getArray(int columnIndex) throws SQLException {
    return this.delegate.getArray(columnIndex);
  }

  @Override
  public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
    return this.delegate.getObject(columnLabel, map);
  }

  @Override
  public Ref getRef(String columnLabel) throws SQLException {
    return this.delegate.getRef(columnLabel);
  }

  @Override
  public Blob getBlob(String columnLabel) throws SQLException {
    return this.delegate.getBlob(columnLabel);
  }

  @Override
  public Clob getClob(String columnLabel) throws SQLException {
    return this.delegate.getClob(columnLabel);
  }

  @Override
  public Array getArray(String columnLabel) throws SQLException {
    return this.delegate.getArray(columnLabel);
  }

  @Override
  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    return this.delegate.getDate(columnIndex, cal);
  }

  @Override
  public Date getDate(String columnLabel, Calendar cal) throws SQLException {
    return this.delegate.getDate(columnLabel, cal);
  }

  @Override
  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    return this.delegate.getTime(columnIndex, cal);
  }

  @Override
  public Time getTime(String columnLabel, Calendar cal) throws SQLException {
    return this.delegate.getTime(columnLabel, cal);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
    return this.delegate.getTimestamp(columnIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
    return this.delegate.getTimestamp(columnLabel, cal);
  }

  @Override
  public URL getURL(int columnIndex) throws SQLException {
    return this.delegate.getURL(columnIndex);
  }

  @Override
  public URL getURL(String columnLabel) throws SQLException {
    return this.delegate.getURL(columnLabel);
  }

  @Override
  public void updateRef(int columnIndex, Ref x) throws SQLException {
    this.delegate.updateRef(columnIndex, x);
  }

  @Override
  public void updateRef(String columnLabel, Ref x) throws SQLException {
    this.delegate.updateRef(columnLabel, x);
  }

  @Override
  public void updateBlob(int columnIndex, Blob x) throws SQLException {
    this.delegate.updateBlob(columnIndex, x);
  }

  @Override
  public void updateBlob(String columnLabel, Blob x) throws SQLException {
    this.delegate.updateBlob(columnLabel, x);
  }

  @Override
  public void updateClob(int columnIndex, Clob x) throws SQLException {
    this.delegate.updateClob(columnIndex, x);
  }

  @Override
  public void updateClob(String columnLabel, Clob x) throws SQLException {
    this.delegate.updateClob(columnLabel, x);
  }

  @Override
  public void updateArray(int columnIndex, Array x) throws SQLException {
    this.delegate.updateArray(columnIndex, x);
  }

  @Override
  public void updateArray(String columnLabel, Array x) throws SQLException {
    this.delegate.updateArray(columnLabel, x);
  }

  @Override
  public RowId getRowId(int columnIndex) throws SQLException {
    return this.delegate.getRowId(columnIndex);
  }

  @Override
  public RowId getRowId(String columnLabel) throws SQLException {
    return this.delegate.getRowId(columnLabel);
  }

  @Override
  public void updateRowId(int columnIndex, RowId x) throws SQLException {
    this.delegate.updateRowId(columnIndex, x);
  }

  @Override
  public void updateRowId(String columnLabel, RowId x) throws SQLException {
    this.delegate.updateRowId(columnLabel, x);
  }

  @Override
  public int getHoldability() throws SQLException {
    return this.delegate.getHoldability();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return this.delegate.isClosed();
  }

  @Override
  public void updateNString(int columnIndex, String nString) throws SQLException {
    this.delegate.updateNString(columnIndex, nString);
  }

  @Override
  public void updateNString(String columnLabel, String nString) throws SQLException {
    this.delegate.updateNString(columnLabel, nString);
  }

  @Override
  public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    this.delegate.updateNClob(columnIndex, nClob);
  }

  @Override
  public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    this.delegate.updateNClob(columnLabel, nClob);
  }

  @Override
  public NClob getNClob(int columnIndex) throws SQLException {
    return this.delegate.getNClob(columnIndex);
  }

  @Override
  public NClob getNClob(String columnLabel) throws SQLException {
    return this.delegate.getNClob(columnLabel);
  }

  @Override
  public SQLXML getSQLXML(int columnIndex) throws SQLException {
    return this.delegate.getSQLXML(columnIndex);
  }

  @Override
  public SQLXML getSQLXML(String columnLabel) throws SQLException {
    return this.delegate.getSQLXML(columnLabel);
  }

  @Override
  public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    this.delegate.updateSQLXML(columnIndex, xmlObject);
  }

  @Override
  public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    this.delegate.updateSQLXML(columnLabel, xmlObject);
  }

  @Override
  public String getNString(int columnIndex) throws SQLException {
    return this.delegate.getNString(columnIndex);
  }

  @Override
  public String getNString(String columnLabel) throws SQLException {
    return this.delegate.getNString(columnLabel);
  }

  @Override
  public Reader getNCharacterStream(int columnIndex) throws SQLException {
    return this.delegate.getNCharacterStream(columnIndex);
  }

  @Override
  public Reader getNCharacterStream(String columnLabel) throws SQLException {
    return this.delegate.getNCharacterStream(columnLabel);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    this.delegate.updateNCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    this.delegate.updateNCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    this.delegate.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    this.delegate.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    this.delegate.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
    this.delegate.updateAsciiStream(columnLabel, x, length);
  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
    this.delegate.updateBinaryStream(columnLabel, x, length);
  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    this.delegate.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
    this.delegate.updateBlob(columnIndex, inputStream, length);
  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
    this.delegate.updateBlob(columnLabel, inputStream, length);
  }

  @Override
  public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    this.delegate.updateClob(columnIndex, reader, length);
  }

  @Override
  public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    this.delegate.updateClob(columnLabel, reader, length);
  }

  @Override
  public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    this.delegate.updateNClob(columnIndex, reader, length);
  }

  @Override
  public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    this.delegate.updateNClob(columnLabel, reader, length);
  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    this.delegate.updateNCharacterStream(columnIndex, x);
  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    this.delegate.updateNCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    this.delegate.updateAsciiStream(columnIndex, x);
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    this.delegate.updateBinaryStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    this.delegate.updateCharacterStream(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    this.delegate.updateAsciiStream(columnLabel, x);
  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    this.delegate.updateBinaryStream(columnLabel, x);
  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    this.delegate.updateCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    this.delegate.updateBlob(columnIndex, inputStream);
  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    this.delegate.updateBlob(columnLabel, inputStream);
  }

  @Override
  public void updateClob(int columnIndex, Reader reader) throws SQLException {
    this.delegate.updateClob(columnIndex, reader);
  }

  @Override
  public void updateClob(String columnLabel, Reader reader) throws SQLException {
    this.delegate.updateClob(columnLabel, reader);
  }

  @Override
  public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    this.delegate.updateNClob(columnIndex, reader);
  }

  @Override
  public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    this.delegate.updateNClob(columnLabel, reader);
  }

  @Override
  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    return this.delegate.getObject(columnIndex, type);
  }

  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    return this.delegate.getObject(columnLabel, type);
  }

  @Override
  public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength)
      throws SQLException {
    this.delegate.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength)
      throws SQLException {
    this.delegate.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
  }

  @Override
  public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
    this.delegate.updateObject(columnIndex, x, targetSqlType);
  }

  @Override
  public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
    this.delegate.updateObject(columnLabel, x, targetSqlType);
  }

}
