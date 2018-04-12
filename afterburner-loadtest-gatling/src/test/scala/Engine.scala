import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

	val props = new GatlingPropertiesBuilder()
		.dataDirectory(IDEPathHelper.dataDirectory.toString)
		.resultsDirectory(IDEPathHelper.resultsDirectory.toString)
		.bodiesDirectory(IDEPathHelper.bodiesDirectory.toString)
		.binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
        .simulationClass("afterburner.AfterburnerBasicTest")
        .runDescription("Simple basic delay test.")

	Gatling.fromMap(props.build)
}
