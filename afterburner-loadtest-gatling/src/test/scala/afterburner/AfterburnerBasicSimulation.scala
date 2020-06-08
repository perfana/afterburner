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
        .header("perfana-test-run-id", "my-test-run-1")
        .warmUp(baseUrl + "/memory/clear")

    val scn: ScenarioBuilder = scenario("AfterburnerBasicSimulation")
        .exec(http("simple_delay")
            .get("/delay?duration=555")
            .check(status.is(200)))
        .pause(1)
        .exec(http("basket purchase good")
          .post("/basket/purchase")
          .body(StringBody("{ \"customer\": \"Johnny\", \"prices\": [10, 20, 30], \"products\": [\"apple\", \"banana\",\"oranges\"], \"totalPrice\": 60 }"))
          .asJson
          .check(status.is(200)))
        .pause(1)
        .exec(http("basket purchase bad")
          .post("/basket/purchase")
          .body(StringBody("{ \"customer\": \"BadGuy\", \"prices\": [20, 30], \"products\": [\"sushi\", \"icescream\"], \"totalPrice\": 40 }"))
          .asJson
          .check(status.is(400)))
        .pause(1)
        .exec(http("simple_mini_leak")
            .get("/memory/grow?objects=2&items=13")
            .check(status.is(200)))
        .pause(1)
        .exec(http("simple_cpu_burn")
            .get("/cpu/magic-identity-check?matrixSize=133")
            .check(status.is(200)))
        .pause(1)
        .exec(http("memory_churn")
            .get("/memory/churn?objects=1818")
            .check(status.is(200)))
        .pause(1)
        .exec(http("db call")
            .get("/db/connect")
            .check(status.is(200)))
        .pause(1)
        .exec(http("remote_call_many")
            .get("/remote/call-many?count=2")
            .check(status.is(200)))
        .pause(1)
        .exec(http("parallel")
          .get("/parallel")
          .check(status.is(200)))
        .pause(1)
        .exec(http("tcp connect")
          .get("/tcp/connect")
          .check(status.is(200)))
        .pause(1)
        .exec(http("upload-file")
            .post("/files/upload")
            .asMultipartForm
            .bodyPart(
                StringBodyPart("upload", "test from gatling")
                  .fileName("gatling-upload.txt")
            )
        )

    setUp(scn.inject(constantUsersPerSec(12) during 60)).protocols(httpProtocol)
    //setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
