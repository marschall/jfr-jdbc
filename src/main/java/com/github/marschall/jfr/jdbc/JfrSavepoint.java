package com.github.marschall.jfr.jdbc;

import java.sql.SQLException;
import java.sql.Savepoint;

abstract class JfrSavepoint implements Savepoint {
  
  private final Savepoint delegate;

  JfrSavepoint(Savepoint delegate) {
    this.delegate = delegate;
  }
  
  Savepoint getDelegate() {
    return this.delegate;
  }

  @Override
  public int getSavepointId() throws SQLException {
    return this.delegate.getSavepointId();
  }

  @Override
  public String getSavepointName() throws SQLException {
    return this.delegate.getSavepointName();
  }

}

final class JfrUnNamedSavepoint extends JfrSavepoint {

  JfrUnNamedSavepoint(Savepoint delegate) {
    super(delegate);
  }
  
}

final class JfrNamedSavepoint extends JfrSavepoint {
  
  JfrNamedSavepoint(Savepoint delegate) {
    super(delegate);
  }
  
}
