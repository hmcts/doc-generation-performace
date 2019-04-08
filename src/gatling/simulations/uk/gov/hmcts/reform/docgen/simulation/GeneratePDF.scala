package uk.gov.hmcts.reform.docgen.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import simulations.uk.gov.hmcts.reform.docgen.scenarios.postGeneratePDF
import simulations.uk.gov.hmcts.reform.docgen.util.{Environment, Headers}
import uk.gov.hmcts.reform.docgen.scenarios.getFormDefinition

import scala.concurrent.duration._


class GeneratePDF extends Simulation {

  val httpConf = http
    .proxy(Proxy("proxyout.reform.hmcts.net", 8080))
    .baseUrl(Environment.baseURL)
    .headers(Headers.commonHeader)

  val docGenScenarios = List (

    getFormDefinition.getRequest.inject(
      rampUsers(400) during(60 seconds)
    ),

    postGeneratePDF.postUser.inject(
      nothingFor(1 second),
      rampUsers(400) during(60 seconds)
    )
  )


  setUp(docGenScenarios)
    .protocols(httpConf)
    .maxDuration(2 minutes)
    .assertions(
      global.responseTime.max.lt(Environment.maxResponseTime.toInt)
    )
}
