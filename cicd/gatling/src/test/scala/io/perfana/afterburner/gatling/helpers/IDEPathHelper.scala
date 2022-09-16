package io.perfana.afterburner.gatling.helpers

import java.nio.file.Path

import io.gatling.commons.util.PathHelper._

/**
  * Utility object to create the paths used throughout this loadtest script.
  */
object IDEPathHelper {

  val gatlingConfUrl: Path = getClass.getClassLoader.getResource("gatling.conf").toURI
  val projectRootDir = gatlingConfUrl.ancestor(3)

  val mavenSourcesDirectory = projectRootDir / "src" / "test" / "scala"
  val mavenResourcesDirectory = projectRootDir / "src" / "test" / "resources"
  val mavenTargetDirectory = projectRootDir / "target"
  val mavenBinariesDirectory = mavenTargetDirectory / "test-classes"

  val dataDirectory = mavenResourcesDirectory / "data"
  val bodiesDirectory = mavenResourcesDirectory / "request-bodies"

  val recorderOutputDirectory = mavenSourcesDirectory
  val resultsDirectory = mavenTargetDirectory / "results"

  val recorderConfigFile = mavenResourcesDirectory / "recorder.conf"
}