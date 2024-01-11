package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Random


object FlakyCall {

 // Define a feeder with a random request name generator
  val feeder = Iterator.continually(Map("requestName" -> s"flaky_call_${Random.nextInt(60)}"))

  val call = exec(http("${requestName}")
    .get("/flaky?maxRandomDelay=240&flakiness=5")
    .header("perfana-request-name", "${requestName}")
    .header("perfana-test-run-id", "${testRunId}")
    .check(status.is(200))
  )

}
