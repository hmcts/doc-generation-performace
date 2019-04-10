package uk.gov.hmcts.reform.docgen.util
import com.warrenstrange.googleauth.GoogleAuthenticator
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object  S2SCode {
  val otp: String = String.valueOf(new GoogleAuthenticator().getTotpPassword(Env.getS2sSecret))

  val S2SAuthToken =
    exec(http("S2S Auth Token")
      .post(Env.getS2sUrl+"/lease")
      .body(StringBody(
        s"""{
       "microservice": "${Env.getS2sMicroservice}",
        "oneTimePassword": "${otp}"
        }"""
      ))
      .asJson
      .header("Content-Type", "application/json")
      .check(bodyString.saveAs("s2sToken")))
      //.check(jsonPath("$..s2sToken").optional.saveAs("s2sToken")))

        .exec {
          session =>
            println("s2s Token-->::" + session("s2sToken"))
            session
        }

}
