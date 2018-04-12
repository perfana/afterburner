package afterburner

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class AfterburnerBasicSimulation extends Simulation {
    private val baseUrl = "http://localhost:8080"
    private val delayEndpoint = "/delay"
    private val contentType = "application/json"
    private val requestCount = 10000

    val httpProtocol: HttpProtocolBuilder = http
        .baseURL(baseUrl)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .contentTypeHeader(contentType)
        .userAgentHeader("curl/7.54.0")
        .warmUp(baseUrl + "/health")

    val scn: ScenarioBuilder = scenario("AfterburnerBasicSimulation")
        .exec(http("simple_delay")
        .get(delayEndpoint)
        .check(status.is(200)))

    setUp(scn.inject(atOnceUsers(requestCount))).protocols(httpProtocol)
}
