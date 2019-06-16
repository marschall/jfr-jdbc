package com.github.marschall.jfr.jdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Operation")
@Description("A JDBC Operation")
@Category("JDBC")
class JdbcObjectEvent extends Event {

  JdbcObjectEvent() {
    super();
  }

  @Label("Object")
  @Description("The object type executing the operation")
  String operationObject;

  @Label("Operation Name")
  @Description("The name of the JDBC operation")
  String operationName;

  @Label("Query")
  @Description("The SQL query string")
  String query;

}