package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.collection.immutable.Map
import scala.concurrent.duration._

object Database {

    val call1 = exec(http("database_call")
              .get("/remote/call-many?count=1&path=/db/employee/find-by-name?firstName=#{FIRST_NAME}")
              .header("perfana-request-name", "database_call")
              .header("perfana-test-run-id", "#{testRunId}")
              .check(status.is(200)))

    val call2 = exec(http("database_call_manager")
      .get("/remote/call-many?count=1&path=/db/employee/find-by-manager-last-name?lastName=#{LAST_NAME}")
      .header("perfana-request-name", "database_call_manager")
      .header("perfana-test-run-id", "#{testRunId}")
      .check(status.is(200)))
        
}