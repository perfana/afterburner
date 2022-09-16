package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object CallMany {

  val call = exec(http("remote_call_async")
    .get("/remote/call-many?count=3&path=delay")
    .header("perfana-request-name", "remote_call_async")
    .header("perfana-test-run-id", "${testRunId}")
    .check(status.is(200)))

}

