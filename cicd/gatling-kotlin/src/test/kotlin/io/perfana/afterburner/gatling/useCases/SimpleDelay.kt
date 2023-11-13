package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object SimpleDelay {

    val call = exec(http("simple_delay")
        .get("/delay?duration=200")
        .header("perfana-request-name", "simple_delay")
        .header("perfana-test-run-id", "\${testRunId}")
        .check(status.is(200)))

}
