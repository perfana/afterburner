package io.perfana.afterburner.gatling.feeders


import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import io.gatling.jdbc.Predef._

object DatabaseFeeder {

  var mariaDb = jdbcFeeder("jdbc:mysql://afterburner-db-mysql-headless:3306/employees", "afterburner", System.getProperty("employeeDbPassword"), "SELECT DISTINCT first_name as FIRST_NAME from employees.employees;").random()

}
