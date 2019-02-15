package com.github.marschall.jfrjdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.Objects;

class JfrPreparedStatement extends JfrStatement implements PreparedStatement {
  
  private final PreparedStatement delegate;

  JfrPreparedStatement(PreparedStatement delegate) {
    super(delegate);
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }
  
  

}
