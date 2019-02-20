package com.github.marschall.jfrjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

class JfrCallResultSet extends JfrResultSet {

  private boolean closed;

  private JfrCallEvent callEvent;

  JfrCallResultSet(ResultSet delegate, JfrCallEvent callEvent) {
    super(delegate);
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
