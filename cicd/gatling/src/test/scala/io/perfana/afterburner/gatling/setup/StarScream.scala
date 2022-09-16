package io.perfana.afterburner.gatling.setup

import io.perfana.afterburner.gatling.configuration.Configuration
import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import scala.concurrent.duration._

/**
 * This Simulation class is responsible for configuring the Scenarios to run, keeping the active
 * profile and configuration in mind. A lot of what happens here depends on the configuration in 
 * the application.conf file. When changes have to be made to which scenarios to run, that
 * information is gathered in the Scenarios object.
 */
class StarScream extends Simulation {


/**
  * The runnableScenarios list will contain injected versions of all of the scenarios that need to be run.
  * This list is constructed by configuring the scenarios in the baseScenario and optionally prepending the
  * list with some auxiliary scenarios when they are needed.
  */

val runnableScenarios: List[PopulationBuilder] = configureBaseScenarios(List(Configuration.baseScenario))


/**
  * Recurses over the list of scenarios passed into the function and sets them up with the required users, ramp-up,
  * etc.
  * @param scenarios the list of Scenarios to initialize
  * @return a list of PopulationBuilders, ready to be simulated
  */
def configureBaseScenarios(scenarios: List[ScenarioBuilder]): List[PopulationBuilder] = scenarios match {
case Nil => Nil
case sc :: scs => (if (Configuration.isDebugActive) setupSingleDebugScenario(sc) else setupSingleScenario(sc)) :: configureBaseScenarios(scs)
}

/**
  * Injects the required debug settings into a single ScenarioBuilder.
  * @param scn the Scenario to initialize
  * @return the initialized PopulationBuilder
  */
def setupSingleDebugScenario(scn: ScenarioBuilder): PopulationBuilder = scn.inject(
atOnceUsers(1)
).disablePauses

/**
  * Injects the required settings into a single ScenarioBuilder.
  * @param scn the Scenario to initialize
  * @return the initialized PopulationBuilder
  */
def setupSingleScenario(scn: ScenarioBuilder): PopulationBuilder = scn.inject(
rampUsersPerSec(Configuration.initialUsersPerSecond) to Configuration.targetUsersPerSecond during (Configuration.rampupTimeInSeconds),
constantUsersPerSec(Configuration.targetUsersPerSecond) during(Configuration.constantLoadTimeInSeconds)


)
.exponentialPauses


// Go!
setUp(runnableScenarios).protocols(if (Configuration.isDebugActive) Configuration.httpDebugProtocol else Configuration.httpProtocol).maxDuration(Configuration.rampupTimeInSeconds + Configuration.constantLoadTimeInSeconds)


}
