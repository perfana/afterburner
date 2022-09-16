package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object RemoteDelay {

  val call = exec(http("remote_call_delayed")
    .get("/remote/call?path=remote/call?path=delay")
    .header("perfana-request-name", "remote_call_delayed")
    .header("perfana-test-run-id", "${testRunId}")
    .check(status.is(200)))

}

