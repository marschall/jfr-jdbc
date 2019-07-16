package com.github.marschall.jfr.jdbc;

import java.sql.ResultSet;
import java.sql.Statement;

class JfrCallResultSet extends JfrResultSet {

  JfrCallResultSet(Statement parent, ResultSet delegate, JdbcCallEvent callEvent) {
    super(parent, delegate, callEvent);
  }

}
