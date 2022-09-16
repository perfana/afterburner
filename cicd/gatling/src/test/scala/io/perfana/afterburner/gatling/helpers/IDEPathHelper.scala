package io.perfana.afterburner.gatling.helpers

import java.nio.file.{Path, Paths}

object IDEPathHelper {

  private val gatlingConfUrl: Path = Paths.get(getClass.getClassLoader.getResource("gatling.conf").toURI)
  private val projectRootDir = gatlingConfUrl.getParent.getParent.getParent
  private val mavenTargetDirectory = projectRootDir.resolve("target")

  private val mavenSrcTestDirectory = projectRootDir.resolve("src").resolve("test")

  val mavenSourcesDirectory = mavenSrcTestDirectory.resolve("scala")
  val mavenResourcesDirectory = mavenSrcTestDirectory.resolve("resources")
  val mavenBinariesDirectory = mavenTargetDirectory.resolve("test-classes")
  val resultsDirectory = mavenTargetDirectory.resolve("gatling")
  val recorderOutputDirectory = mavenSourcesDirectory
  val recorderConfigFile = mavenResourcesDirectory.resolve("recorder.conf")

  val dataDirectory = mavenResourcesDirectory.resolve("data")
  val bodiesDirectory = mavenResourcesDirectory.resolve("bodies")
}
