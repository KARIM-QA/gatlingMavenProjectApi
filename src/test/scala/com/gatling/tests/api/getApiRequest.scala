package com.gatling.tests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class getApiRequest extends Simulation {

//protocol
private val  httpProtocol = http
  .baseUrl("https://reqres.in/api/users")
//scenario
private val getUserScenario=scenario("Get Api Request")
  .exec(
    http("Get Single User")
      .get("/5")
      .check(
        status.is(200),
        jsonPath("$.data.first_name").is("Charles")
      )


  )
  .pause(2)
//setup
setUp(
  getUserScenario.inject(rampUsers(20).during(5))
    .protocols(httpProtocol)
)

}
