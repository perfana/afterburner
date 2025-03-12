package io.perfana.afterburner.gatling.configuration

import java.util.concurrent.TimeUnit
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.perfana.afterburner.gatling.setup._
import io.perfana.afterburner.gatling.helpers._


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
  System.out.println("targetBaseUrl: " + targetBaseUrl)

  // get testRunId
  val testRunId = System.getProperty("testRunId")

  // get load figures
  val initialUsersPerSecond  = System.getProperty("initialUsersPerSecond").toDouble
  val targetUsersPerSecond  = System.getProperty("targetUsersPerSecond").toDouble
  val rampupTimeInSeconds = ( System.getProperty("rampupTimeInSeconds").toLong, TimeUnit.SECONDS )
  val constantLoadTimeInSeconds = ( System.getProperty("constantLoadTimeInSeconds").toLong, TimeUnit.SECONDS )


  /**
    * This determines what scenario to use as baseScenario based on the profile
    * selected when starting the test
    */

  val scenario = System.getProperty("scenario")

  val baseScenario = scenario match {

    case "debug" => Scenarios.debugScenario
    case "acceptance"  => Scenarios.acceptanceTestScenario
    case "slowbackend"  => Scenarios.slowBackendTestScenario
    case "cpu"  => Scenarios.cpuTestScenario
    case "lotsOfRequestNames"  => Scenarios.lotsOfRequestNamesScenario

    case _ => Scenarios.acceptanceTestScenario


  }

  private val baseHttpProtocol = http
    .baseUrl(Configuration.targetBaseUrl)
    .maxRedirects(2)
    .acceptHeader("""*/*""")
    .acceptEncodingHeader("""gzip,deflate,sdch""")
    .acceptLanguageHeader("""en-US,en;q=0.8,nl;q=0.6""")
    .contentTypeHeader("""application/json;charset=UTF-8""")
    .userAgentHeader("""Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36""")
    .inferHtmlResources(AllowList(""".*""" + Configuration.targetBaseUrl + """.*"""), DenyList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""))


  private val baseHttpDebugProtocol = http
    .baseUrl(Configuration.targetBaseUrl)
    .acceptHeader("""*/*""")
    .acceptEncodingHeader("""gzip,deflate,sdch""")
    .acceptLanguageHeader("""en-US,en;q=0.8,nl;q=0.6""")
    .contentTypeHeader("""application/json;charset=UTF-8""")
    .userAgentHeader("""Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36""")
    .inferHtmlResources(AllowList(""".*""" + Configuration.targetBaseUrl + """.*"""), DenyList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""))

  def httpDebugProtocol ={
    /* Add proxy if specified */
    if(Configuration.useProxy) {
      System.out.println("Using proxy at localhost port 8888!")
      baseHttpDebugProtocol.proxy(Proxy("localhost", 8888))
    }else{
      baseHttpDebugProtocol
    }
  }

  def httpProtocol ={
    baseHttpDebugProtocol
  }

  // dump all configuration to log

  val dump = ConfigurationDumper.values // just here to dump config before code below is instantiated

}
