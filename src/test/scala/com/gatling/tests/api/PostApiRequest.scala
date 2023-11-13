package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostApiRequest extends Simulation {

  //protocol
  private val httpProtocol = http
    .baseUrl("https://reqres.in/api")
    .contentTypeHeader("application/json")

  //Scenario
  private val postUserScenario = scenario("Create User")
    .exec(
      http("User Post Request")
        .post("/users")
        .body(StringBody(
          """{
            |  "name": "king",
            |  "job": "SDET ENGINEER"
            |}""".stripMargin
        ))
        .check(
          status.is(201),
          jsonPath("$.name").is("king"),
          jsonPath("$.job").is("SDET ENGINEER")
        )
    )
    .pause(2)


  //Setup
  setUp(
    postUserScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
