package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*

object CallMany {

  val call = exec(http("remote_call_async")
    .get("/remote/call-many?count=5&path=delay?duration=222")
    .header("perfana-request-name", "remote_call_async")
    .header("perfana-test-run-id", "\${testRunId}")
    .check(status.is(200)))

}
