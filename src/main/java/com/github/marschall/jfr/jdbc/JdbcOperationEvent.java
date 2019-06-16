package com.github.marschall.jfr.jdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Object")
@Description("A JDBC Object")
@Category("JDBC")
class JdbcOperationEvent extends Event {

  @Label("Type")
  @Description("The object type")
  private String type;

  String getType() {
    return this.type;
  }

  void setType(String type) {
    this.type = type;
  }

}