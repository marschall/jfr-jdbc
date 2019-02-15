package com.github.marschall.jfrjdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Operation")
@Description("A JDBC Operation")
@Category("JDBC")
class JdbcObjectEvent extends Event {
  
  @Label("Object")
  @Description("The object type executing the operation")
  private String operationObject;

  @Label("Operation Name")
  @Description("The name of the JDBC operation")
  private String operationName;

  @Label("Query")
  @Description("The SQL query string")
  private String query;

  String getOperationName() {
    return this.operationName;
  }

  void setOperationName(String operationName) {
    this.operationName = operationName;
  }

  String getQuery() {
    return this.query;
  }

  void setQuery(String query) {
    this.query = query;
  }

}