module com.github.marschall.jfr.jdbc {

  requires transitive java.sql;
  requires transitive java.logging;

  requires jdk.jfr;

  exports com.github.marschall.jfr.jdbc;

  provides java.sql.Driver with com.github.marschall.jfr.jdbc.JfrDriver;

}