module com.github.marschall.jfrjdbc {

  requires transitive java.sql;
  requires transitive java.logging;

  requires jdk.jfr;

  exports com.github.marschall.jfrjdbc;
  
  provides java.sql.Driver with com.github.marschall.jfrjdbc.JfrDriver;

}