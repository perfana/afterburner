package io.perfana.afterburner.gatling.setup

import io.perfana.afterburner.gatling.configuration.Configuration
import io.perfana.afterburner.gatling.useCases._
import io.perfana.afterburner.gatling.feeders._
import io.gatling.core.Predef._
import test.scala.io.perfana.afterburner.gatling.useCases.FlakyCallDynamicRequestName
import scala.util.Random

import scala.concurrent.duration._

/**
 * This object collects the Scenarios in the project for use in the Simulation. There are two
 * main properties in this object: acceptanceTestScenario and debugScenario. These two are
 * used in the Simulation class to setup the actual tests to run. If you wish to add
 * scenarios to either run, add them here.
 */
object Scenarios {

  /**
   * These are the scenarios run in 'normal' mode.
   */
  val acceptanceTestScenario = scenario("Acceptance test")
    .feed(CSVFeeder.firstName)
    .feed(CSVFeeder.lastName)
    .exec(session => session.set("testRunId", Configuration.testRunId))
     .exec(SimpleCpuBurn.call)
     .pause(3)
      .exec(SimpleDelay.call)
//    .pause(3)
//    .exec(SimpleMiniLeak.call)
//    .pause(3)
//    .exec(UploadFile.call)
//    .pause(3)
//    .exec(RemoteDelay.call)
      .pause(3)
      .exec(CallMany.call)
    .pause(3)
//    .exec(Database.call1)
    .pause(1)
    .randomSwitch(
      5.0 -> exec(Database.call2),
    )
    .pause(1)
    .exec(FlakyCall.call)

  // Define a feeder with a random request name generator
  val requestNameFeeder = Iterator.continually(Map("requestName" -> s"flaky_call_#{Random.nextInt(60)}"))

  val lotsOfRequestNamesScenario = scenario("Lots of request names test")
  .feed(CSVFeeder.firstName)
  .feed(CSVFeeder.lastName)
    .exec(session => session.set("testRunId", Configuration.testRunId))
     .exec(SimpleCpuBurn.call)
     .pause(3)
      .exec(SimpleDelay.call)
      .pause(3)
      .exec(CallMany.call)
      .pause(1)
      .exec(Database.call1)
      .pause(1)
      .exec(Database.call2)
      .pause(3)
      .feed(requestNameFeeder)
      .exec(FlakyCallDynamicRequestName.call)

  val slowBackendTestScenario = scenario("Slow backend test")
.feed(CSVFeeder.firstName)
    .exec(session => session.set("testRunId", Configuration.testRunId))
    .exec(RemoteDelay.call)
    .pause(3)
    .exec(CallMany.call)
    .pause(3)
//    .exec(Database.call1)
//    .pause(1)
//    .exec(Database.call2)
//    .pause(1)
    .exec(FlakyCall.call)

  val cpuTestScenario = scenario("CPU test")
.feed(CSVFeeder.firstName)
    .exec(session => session.set("testRunId", Configuration.testRunId))
    .exec(SimpleCpuBurnPlus.call)
    .pause(3)
    .exec(SimpleDelay.call)
    .pause(3)
    .exec(CallMany.call)
    .pause(3)
  //    .exec(Database.call1)
  //    .pause(1)
  //    .exec(Database.call2)
  //    .pause(1)

  /**
   * These are the scenarios run in 'debug' mode.
   */
  val debugScenario = scenario("debug")
    .exec(session => session.set("testRunId", Configuration.testRunId))
    .repeat(100) {
      exec(FlakyCall.call)
    }
//
.feed(CSVFeeder.firstName)
//    .exitBlockOnFail(
//      pause(3)
//        .exec(Database.call)
//    )


}
