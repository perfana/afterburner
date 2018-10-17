package afterburner

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class AfterburnerBasicSimulation extends Simulation {
    private val baseUrl = "http://localhost:8080"
    private val contentType = "application/json"

    val httpProtocol: HttpProtocolBuilder = http
        .baseUrl(baseUrl)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .contentTypeHeader(contentType)
        .userAgentHeader("curl/7.54.0")
        .warmUp(baseUrl + "/memory/clear")

    val scn: ScenarioBuilder = scenario("AfterburnerBasicSimulation")
        .exec(http("simple_delay")
            .get("/delay?duration=555")
            .check(status.is(200)))
        .pause(3)
        .exec(http("simple_mini_leak")
            .get("/memory/grow?objects=8&items=54")
            .check(status.is(200)))
        .pause(2)
        .exec(http("simple_cpu_burn")
            .get("/cpu/magic-identity-check?matrixSize=133")
            .check(status.is(200)))
        .pause(3)
        .exec(http("upload-file")
            .post("/files/upload")
            .asMultipartForm
            .bodyPart(
                StringBodyPart("upload", "test from gatling")
                  .fileName("gatling-upload.txt")
            )
        )


    //setUp(scn.inject(constantUsersPerSec(12) during 30)).protocols(httpProtocol)
    setUp(scn.inject(constantUsersPerSec(1) during 1)).protocols(httpProtocol)
}
