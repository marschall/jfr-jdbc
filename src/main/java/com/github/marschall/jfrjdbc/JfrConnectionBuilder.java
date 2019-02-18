package com.github.marschall.jfrjdbc;

import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.ShardingKey;
import java.util.Objects;

final class JfrConnectionBuilder implements ConnectionBuilder {
  
  private final ConnectionBuilder delegate;

  JfrConnectionBuilder(ConnectionBuilder delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  public ConnectionBuilder user(String username) {
    return delegate.user(username);
  }

  public ConnectionBuilder password(String password) {
    return delegate.password(password);
  }

  public ConnectionBuilder shardingKey(ShardingKey shardingKey) {
    return delegate.shardingKey(shardingKey);
  }

  public ConnectionBuilder superShardingKey(ShardingKey superShardingKey) {
    return delegate.superShardingKey(superShardingKey);
  }

  public Connection build() throws SQLException {
    var event = new JdbcObjectEvent();
    event.operationObject = "ConnectionBuilder";
    event.operationName = "build";
    try {
      Connection connection = delegate.build();
      return new JfrConnection(connection);
    } finally {
      event.end();
      event.commit();
    }
  }


}
