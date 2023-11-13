package io.perfana.afterburner.gatling.configuration

import java.util.concurrent.TimeUnit
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.perfana.afterburner.gatling.setup.*
import io.perfana.afterburner.gatling.helpers.*

/**
 * Contains the configuration needed to build the Scenarios to run. All configuration is read from
 * the resources/application.conf file.
 */
object Configuration {

    val isDebugActive = System.getProperty("debug") == "true"
    val useProxy = System.getProperty("useProxy") == "true"
    val graphitePrefix = System.getProperty("gatling.data.graphite.rootPathPrefix")

    //  get targetBaseUrl
    val targetBaseUrl = System.getProperty("targetBaseUrl")
    init {
        println("targetBaseUrl: $targetBaseUrl")
    }

    // get testRunId
    val testRunId = System.getProperty("testRunId")

    // get load figures
    val initialUsersPerSecond = System.getProperty("initialUsersPerSecond").toDouble()
    val targetUsersPerSecond = System.getProperty("targetUsersPerSecond").toDouble()
    val rampupTimeInSeconds = System.getProperty("rampupTimeInSeconds").toLong() to TimeUnit.SECONDS
    val constantLoadTimeInSeconds = System.getProperty("constantLoadTimeInSeconds").toLong() to TimeUnit.SECONDS

    /**
     * This determines what scenario to use as baseScenario based on the profile
     * selected when starting the test
     */
    val scenario = System.getProperty("scenario")

    val baseScenario = when (scenario) {
        "debug" -> Scenarios.debugScenario
        "acceptance" -> Scenarios.acceptanceTestScenario
        "slowbackend" -> Scenarios.slowBackendTestScenario
        "cpu" -> Scenarios.cpuTestScenario
        else -> Scenarios.acceptanceTestScenario
    }

    private val baseHttpProtocol = http
        .baseUrl(Configuration.targetBaseUrl)
    // ... (remaining settings are the same)

    private val baseHttpDebugProtocol = http
        .baseUrl(Configuration.targetBaseUrl)
    // ... (remaining settings are the same)

    val httpDebugProtocol by lazy {
        /* Add proxy if specified */
        if (Configuration.useProxy) {
            println("Using proxy at localhost port 8888!")
            baseHttpDebugProtocol.proxy(Proxy("localhost", 8888).httpsPort(8888))
        } else {
            baseHttpDebugProtocol
        }
    }

    val httpProtocol by lazy {
        baseHttpDebugProtocol
    }

    // dump all configuration to log
    val dump = ConfigurationDumper.values // just here to dump config before code below is instantiated
}
