package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DeleteApiRequestWithBearerAuthorization_Single_User extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"
  private val userId: Int = 5711263

  //protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  //Scenario
  val deleteUserScenario = scenario("Delete a single User")
    .exec(
      http("User Delete Request")
        .delete(s"/users/${userId}")
        .check(
          status.is(204)

        )
        .check(bodyString.saveAs("responseBody")) // Save the response body
    )
    .exec(session => {
      println(session("responseBody").as[String]) // Print the response body
      session
    })
    .pause(2)

  //Setup
  setUp(
    deleteUserScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
