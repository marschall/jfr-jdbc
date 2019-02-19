package com.github.marschall.jfrjdbc;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JDBC Call")
@Description("A JDBC Call")
@Category("JDBC")
class JfrCallEvent extends Event {

  JfrCallEvent(String query) {
    this.query = query;
  }

  @Label("Query")
  @Description("The SQL query string")
  String query;

}
