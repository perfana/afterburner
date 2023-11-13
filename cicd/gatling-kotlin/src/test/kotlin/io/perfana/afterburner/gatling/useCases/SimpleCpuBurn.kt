package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object SimpleCpuBurn {

    val call = exec(http("simple_cpu_burn")
        .get("/cpu/magic-identity-check?matrixSize=133")
        .header("perfana-request-name", "simple_cpu_burn")
        .header("perfana-test-run-id", "\${testRunId}")
        .check(status.is(200)))

}
