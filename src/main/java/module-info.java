module com.github.marschall.jfrjdbc {

  requires transitive java.sql;
  requires transitive java.logging;

  requires jdk.jfr;

  exports com.github.marschall.jfrjdbc;

}