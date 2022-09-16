package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object CallMany {

  val call = exec(http("call_many")
    .get("/remote/call-many?path=delay")
    .header("perfana-request-name", "call_many")
    .header("perfana-test-run-id", "${testRunId}")
    .check(status.is(200)))

}

