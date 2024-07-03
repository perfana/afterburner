package io.perfana.afterburner.gatling.feeders


import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object CSVFeeder {

  val firstName = csv("data/firstNames.csv").random
  val lastName = csv("data/lastNames.csv").random

}