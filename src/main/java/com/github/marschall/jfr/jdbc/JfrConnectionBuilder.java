package com.github.marschall.jfr.jdbc;

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

  @Override
  public ConnectionBuilder user(String username) {
    return this.delegate.user(username);
  }

  @Override
  public ConnectionBuilder password(String password) {
    return this.delegate.password(password);
  }

  @Override
  public ConnectionBuilder shardingKey(ShardingKey shardingKey) {
    return this.delegate.shardingKey(shardingKey);
  }

  @Override
  public ConnectionBuilder superShardingKey(ShardingKey superShardingKey) {
    return this.delegate.superShardingKey(superShardingKey);
  }

  @Override
  public Connection build() throws SQLException {
    var event = new JdbcOperationEvent();
    event.operationObject = "ConnectionBuilder";
    event.operationName = "build";
    try {
      Connection connection = this.delegate.build();
      return new JfrConnection(connection);
    } finally {
      event.end();
      event.commit();
    }
  }


}
