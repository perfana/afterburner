package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object SimpleMiniLeak {

    val call = exec(http("simple_mini_leak")
        .get("/memory/grow?objects=8&items=54")
        .header("perfana-request-name", "simple_mini_leak")
        .header("perfana-test-run-id", "\${testRunId}")
        .check(status.is(200)))

}
