package io.perfana.afterburner.gatling.setup

import io.perfana.afterburner.gatling.configuration.Configuration
import io.perfana.afterburner.gatling.useCases._
import io.perfana.afterburner.gatling.feeders._
import io.gatling.core.Predef._
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
    .exec(session => session.set("testRunId", Configuration.testRunId))  
    .exec(SimpleCpuBurn.call)
    .pause(3)
    .exec(SimpleDelay.call)
//    .pause(3)
//    .exec(SimpleMiniLeak.call)
    .pause(3)
    .exec(UploadFile.call)
    .pause(3)
    .exec(RemoteDelay.call)
    .pause(3)
    .exec(CallMany.call)

    

  /**
   * These are the scenarios run in 'debug' mode.
   */
  val debugScenario = scenario("debug")
    .exitBlockOnFail(
      exec(SimpleCpuBurn.call)
    )


}