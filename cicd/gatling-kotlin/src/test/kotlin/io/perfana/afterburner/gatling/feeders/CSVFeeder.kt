package io.perfana.afterburner.gatling.feeders

import scala.concurrent.duration.*

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

/**
 * Created by x077411 on 12/12/2014.
 */
object CSVFeeder {

    val firstName = csv("data/firstNames.csv").random()
}
