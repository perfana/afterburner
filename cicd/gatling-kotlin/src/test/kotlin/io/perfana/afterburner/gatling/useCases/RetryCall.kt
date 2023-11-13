package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object RetryCall {

    val call = exec(http("flaky_call")
        .get("/remote/call-retry?path=flaky?maxRandomDelay=0&flakiness=5")
        .header("perfana-request-name", "flaky_call")
        .header("perfana-test-run-id", "\${testRunId}")
        .check(status.is(200))
    )

}
