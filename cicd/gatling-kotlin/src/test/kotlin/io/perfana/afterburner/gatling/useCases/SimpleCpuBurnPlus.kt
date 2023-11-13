package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object SimpleCpuBurnPlus {

    val call = exec(http("simple_cpu_burn")
        .get("/cpu/magic-identity-check?matrixSize=256")
        .header("perfana-request-name", "matrix_calculation")
        .header("perfana-test-run-id", "\${testRunId}")
        .check(status.is(200)))

}
