package io.perfana.afterburner.gatling.useCases

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

object UploadFile {

    val call = exec(http("upload-file")
        .post("/files/upload")
        .asMultipartForm()
        .bodyPart(
            StringBodyPart("upload", "test from gatling")
                .fileName("gatling-upload.txt")
        )
        .header("perfana-request-name", "upload-file")
        .header("perfana-test-run-id", "\${testRunId}")
    )

}
