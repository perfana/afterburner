package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.collection.immutable.Map
import scala.concurrent.duration._

object UploadFile {

  val call = exec(http("upload-file")
            .post("/files/upload")
            .asMultipartForm
            .bodyPart(
                StringBodyPart("upload", "test from gatling")
                  .fileName("gatling-upload.txt")
            )
            .header("perfana-request-name", "upload-file")
            .header("perfana-test-run-id", "${testRunId}")

        )

}