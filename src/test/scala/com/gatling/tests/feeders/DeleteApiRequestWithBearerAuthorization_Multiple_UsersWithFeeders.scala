package com.gatling.tests.feeders

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*

class DeleteApiRequestWithBearerAuthorization_Multiple_UsersWithFeeders extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"

  // Read user IDs from the feeder
  val userIdFeeder = csv("data/userIds.csv").queue

  //protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  //Scenario
  val deleteUserScenario = scenario("Delete Multiple Users using feeders")
    .feed(userIdFeeder)
    .exec(
      http("User Delete Request")
        .delete("/users/${userId}")
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
    deleteUserScenario.inject(atOnceUsers(5))
  ).protocols(httpProtocol)
}
