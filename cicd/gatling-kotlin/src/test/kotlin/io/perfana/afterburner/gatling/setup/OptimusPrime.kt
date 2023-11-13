package io.perfana.afterburner.gatling.setup

import io.perfana.afterburner.gatling.configuration.Configuration
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import scala.concurrent.duration.*

/**
 * This Simulation class is responsible for configuring the Scenarios to run, keeping the active
 * profile and configuration in mind. A lot of what happens here depends on the configuration in
 * the application.conf file. When changes have to be made to which scenarios to run, that
 * information is gathered in the Scenarios object.
 */
class OptimusPrime : Simulation() {

    /**
     * The runnableScenarios list will contain injected versions of all of the scenarios that need to be run.
     * This list is constructed by configuring the scenarios in the baseScenario and optionally prepending the
     * list with some auxiliary scenarios when they are needed.
     */
    private val runnableScenarios = configureBaseScenarios(listOf(Configuration.baseScenario))

    /**
     * Recurses over the list of scenarios passed into the function and sets them up with the required users, ramp-up,
     * etc.
     * @param scenarios the list of Scenarios to initialize
     * @return a list of PopulationBuilders, ready to be simulated
     */
    private fun configureBaseScenarios(scenarios: List<ScenarioBuilder>): List<PopulationBuilder> =
        scenarios.flatMap { scn ->
            listOf(if (Configuration.isDebugActive) setupSingleDebugScenario(scn) else setupSingleScenario(scn))
        }

    /**
     * Injects the required debug settings into a single ScenarioBuilder.
     * @param scn the Scenario to initialize
     * @return the initialized PopulationBuilder
     */
    private fun setupSingleDebugScenario(scn: ScenarioBuilder): PopulationBuilder = scn.inject(
        atOnceUsers(1)
    ).disablePauses()

    /**
     * Injects the required settings into a single ScenarioBuilder.
     * @param scn the Scenario to initialize
     * @return the initialized PopulationBuilder
     */
    private fun setupSingleScenario(scn: ScenarioBuilder): PopulationBuilder = scn.inject(
        rampUsersPerSec(Configuration.initialUsersPerSecond).to(Configuration.targetUsersPerSecond).during(Configuration.rampupTimeInSeconds),
        constantUsersPerSec(Configuration.targetUsersPerSecond).during(Configuration.constantLoadTimeInSeconds)
    ).exponentialPauses()

    // Go!
    init {
        setUp(runnableScenarios)
            .protocols(if (Configuration.isDebugActive) Configuration.httpDebugProtocol else Configuration.httpProtocol)
            .maxDuration(Configuration.rampupTimeInSeconds + Configuration.constantLoadTimeInSeconds)
    }
}
