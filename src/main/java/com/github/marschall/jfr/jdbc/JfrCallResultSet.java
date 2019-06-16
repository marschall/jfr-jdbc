package com.github.marschall.jfr.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class JfrCallResultSet extends JfrResultSet {

  private boolean closed;

  private JfrCallEvent callEvent;

  JfrCallResultSet(Statement parent, ResultSet delegate, JfrCallEvent callEvent) {
    super(parent, delegate);
    this.callEvent = callEvent;
    this.closed = false;
  }

  @Override
  public void close() throws SQLException {
    if (!this.closed) {
      this.callEvent.end();
      this.callEvent.commit();
      this.callEvent = null;
      this.closed = true;
    }
    this.delegate.close();
  }

}
