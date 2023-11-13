package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object FlakyCall {

  val call = exec(http("flaky_call")
    .get("/flaky?maxRandomDelay=240&flakiness=5")
    .header("perfana-request-name", "flaky_call")
    .header("perfana-test-run-id", "\${testRunId}")
    .check(status.is(200))
  )

}
