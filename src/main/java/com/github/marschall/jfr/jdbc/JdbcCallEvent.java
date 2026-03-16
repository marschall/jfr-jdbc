package com.github.marschall.jfr.jdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Call")
@Description("A JDBC Call")
@Category("JDBC")
class JdbcCallEvent extends Event {
  
  @Label("Query")
  @Description("The SQL query string")
  String query;

  @Label("Row Count")
  @Description("The number of rows returned or updated")
  // long instead of int to avoid overflows for batch updates
  long rowCount;
  
  transient boolean closed = false;

  JdbcCallEvent(String query) {
    this.query = query;
  }

  void close() {
    if (!closed) {
      closed = true;
      end();
      commit();
    }
  }
}
