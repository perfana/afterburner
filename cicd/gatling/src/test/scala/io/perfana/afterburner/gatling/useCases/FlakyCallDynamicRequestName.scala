package test.scala.io.perfana.afterburner.gatling.useCases


import io.gatling.core.Predef._
import io.gatling.http.Predef._
object FlakyCallDynamicRequestName {



  val call = exec(http("#{requestName}")
    .get("/flaky?maxRandomDelay=240&flakiness=5")
    .header("perfana-request-name", "#{requestName}")
    .header("perfana-test-run-id", "#{testRunId}")
    .check(status.is(200))
  )

}
