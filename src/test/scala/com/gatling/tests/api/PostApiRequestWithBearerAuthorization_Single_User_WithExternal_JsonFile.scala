package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class PostApiRequestWithBearerAuthorization_Single_User_WithExternal_JsonFile extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"

  //protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  //Scenario
  val postUsersScenario = scenario("Create User Based in External Json File")
    .exec(
      http("User Post Request")
        .post("/users")
        .body(RawFileBody("data/user.json"))
        .check(
          //status.is(201),
          jsonPath("$.name").is("Karimou"),
          jsonPath("$.email").is("karimoukingOfAutomation211244441@gmail.com")
        )
        .check(bodyString.saveAs("responseBody")) // Add this line to save the response body
    )
    .exec(session => {
      println(session("responseBody").as[String]) // Print the response body
      session
    })


  //Setup

  setUp(
    postUsersScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
