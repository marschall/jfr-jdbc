package com.github.marschall.jfr.jdbc;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Relational;

@Label("Object Id")
@Description("Id to track which object an operation belongs to")
@Relational
@Target(FIELD)
@Retention(RUNTIME)
@interface ObjectId {

}