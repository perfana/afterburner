package io.perfana.afterburner.gatling.feeders


import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

/**
 * Created by x077411 on 12/12/2014.
 */
object SomeDataFeeder {

  val someData = csv("someData.csv").random

}
