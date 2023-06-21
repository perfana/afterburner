package afterburner

import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.time.Duration

class AfterburnerBasicSimulation : Simulation() {

    private val baseUrl = System.getenv()["targetBaseUrl"] ?: "http://localhost:3535"

    private val contentType = "application/json"

    val httpProtocol = http
        .baseUrl(baseUrl)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .contentTypeHeader(contentType)
        .userAgentHeader("curl/7.54.0")
        .header("perfana-test-run-id", "my-test-run-1")
        .warmUp("$baseUrl/memory/clear")

    val scn = scenario("AfterburnerBasicSimulation")
        .exec(http("simple_delay")
            .get("/delay?duration=222")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("basket purchase good")
            .post("/basket/purchase")
            .body(StringBody("{ \"customer\": \"Johnny\", \"prices\": [10, 20, 30], \"products\": [\"apple\", \"banana\",\"oranges\"], \"totalPrice\": 60 }"))
            .asJson()
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("basket purchase bad")
            .post("/basket/purchase")
            .body(StringBody("{ \"customer\": \"BadGuy\", \"prices\": [20, 30], \"products\": [\"sushi\", \"icescream\"], \"totalPrice\": 40 }"))
            .asJson()
            .check(status().shouldBe(400)))
        .pause(1)
        .exec(http("basket store")
            .post("/basket/store")
            .body(StringBody("{ \"customer\": \"BadGuy\", \"prices\": [20, 30], \"products\": [\"sushi\", \"icescream\"], \"totalPrice\": 40 }"))
            .asJson()
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("basket find all")
            .get("/basket/all")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("simple_mini_leak")
            .get("/memory/grow?objects=2&items=130")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("simple_cpu_burn")
            .get("/cpu/magic-identity-check?matrixSize=133")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("memory_churn")
            .get("/memory/churn?objects=1818")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("db call")
            .get("/db/connect")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("remote_call_many")
            .get("/remote/call-many?count=2")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("parallel")
            .get("/parallel")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("tcp connect")
            .get("/tcp/connect")
            .check(status().shouldBe(200)))
        .pause(1)
        .exec(http("upload-file")
            .post("/files/upload")
            .asMultipartForm()
            .bodyPart(
                StringBodyPart("upload", "test from gatling")
                    .fileName("gatling-upload.txt")
            )
        )

    init {
        setUp(
            scn.injectOpen(constantUsersPerSec(6.0).during(Duration.ofSeconds(120))
            ).protocols(httpProtocol))
    }

}
