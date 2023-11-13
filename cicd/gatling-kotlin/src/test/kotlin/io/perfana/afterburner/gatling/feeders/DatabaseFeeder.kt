package io.perfana.afterburner.gatling.feeders

import scala.concurrent.duration.*

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*
import io.gatling.jdbc.Predef.*

/**
 * Created by x077411 on 12/12/2014.
 */
object DatabaseFeeder {

    var mariaDb = jdbcFeeder("jdbc:mysql://afterburner-db-mysql-headless:3306/employees", "afterburner", System.getProperty("employeeDbPassword"), "SELECT DISTINCT first_name as FIRST_NAME from employees.employees").random()

}
