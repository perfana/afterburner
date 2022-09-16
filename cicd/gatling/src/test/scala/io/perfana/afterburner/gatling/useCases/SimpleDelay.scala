package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.collection.immutable.Map
import scala.concurrent.duration._
import scala.util.Random


object SimpleDelay {

  
  val call = exec(session => session.set("randomDelay", Random.nextInt(10) + 485))
            .exec(http("simple_delay")
            .get("/delay?duration=${randomDelay}")
            .header("perfana-request-name", "simple_delay")
            .header("perfana-test-run-id", "${testRunId}")
            .check(status.is(200)))
        
}
