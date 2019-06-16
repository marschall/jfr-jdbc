JFR JDBC [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/jfr-jdbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/jfr-jdbc) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/jfr-jdbc.svg)](https://www.javadoc.io/doc/com.github.marschall/jfr-jdbc)
========

A JDBC driver that wraps an other JDBC driver and generates JFR events.

Usage
-----

Simply wrap your `DataSource` with a `JfrDataSource`

```java
new JfrDataSource(dataSource)
```

The project is a Java module with the name `com.github.marschall.jfr.jdbc`.

Implementation/Overhead
-----------------------

The implementation is based around wrapper objects.

* no reflection
* no string concatenation

Caveats
-------

- does not work with Oracle explicit statement caching
- does not work with unwrapped objects
- does not work with implicitly closed objects
