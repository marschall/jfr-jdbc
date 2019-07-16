package com.github.marschall.jfr.jdbc;

import java.util.concurrent.atomic.AtomicLong;

final class ObjectIdGenerator {

  private static final AtomicLong CURRENT_ID = new AtomicLong();

  private ObjectIdGenerator() {
    throw new AssertionError("not instantiable");
  }

  static long nextId() {
    return CURRENT_ID.incrementAndGet();
  }

}
