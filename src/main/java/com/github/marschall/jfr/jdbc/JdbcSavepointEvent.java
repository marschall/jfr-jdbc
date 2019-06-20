package com.github.marschall.jfr.jdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Savepoint")
@Description("A JDBC Savepoint")
@Category("JDBC")
class JdbcSavepointEvent extends Event {

  JdbcSavepointEvent() {
    super();
  }

  @Label("Savepoint Id")
  @Description("The id of the Savepoint")
  int savepointId;

  @Label("Savepoint Name")
  @Description("The name of the Savepoint")
  String savepointName;

  @Label("Operation Name")
  @Description("The name of the JDBC operation")
  String operationName;

}