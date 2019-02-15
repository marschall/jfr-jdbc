package com.github.marschall.jfrjdbc;

import java.sql.CallableStatement;
import java.util.Objects;

final class JfrCallableStatement extends JfrPreparedStatement implements CallableStatement {
  
  private final CallableStatement delegate;

  JfrCallableStatement(CallableStatement delegate) {
    super(delegate);
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

}
