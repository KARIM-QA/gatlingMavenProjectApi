package com.gatling.tests.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostApiRequestWithBearerAuthorization_Multi_usersWithFeeders extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"

  //protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  //scenario
  val feeder = csv("data/dataToInject.csv").circular

  val feederScenario = scenario("Inject Multiple users data Using Feeders")
    .feed(feeder)

    // Print the feeders to console
    .exec(session => {
      println("name : " + session("name").as[String])
      println("email :" + session("email").as[String])
      println("gender :" + session("gender").as[String])
      println("status :" + session("status").as[String])
      session
    })
    .exec(
      http("User Post Request")
        .post("/users")
        .body(StringBody(
          s"""{
             |  "name" : "$${name}",
             |  "email" : "$${email}",
             |  "gender" : "$${gender}",
             |  "status" : "$${status}"
             |}
             |""".stripMargin)
        )

    )

    .pause(2)



  //setup
  setUp(feederScenario.inject(atOnceUsers(5))).protocols(httpProtocol)

}
