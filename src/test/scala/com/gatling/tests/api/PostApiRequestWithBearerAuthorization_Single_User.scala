package com.gatling.tests.api


import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class PostApiRequestWithBearerAuthorization_Single_User extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"

  // Function to generate a random email
  def getRandomEmail: String = {
    val emailId = s"karim.automation${System.currentTimeMillis()}@gmail.com"
    emailId
  }

  //protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  //Scenario
  val postUsersScenario = scenario("Create a single User")
    .exec(session => {
      // Use the generated random email in the request
      val randomEmail = getRandomEmail
      session.set("randomEmail", randomEmail)
    })
    .exec(
      http("User Post Request")
        .post("/users")
        .body(StringBody(
          s"""{
            |  "name" : "Karimou32541",
            |  "email" : "$${randomEmail}",
            |  "gender" : "male",
            |  "status" : "active"
            |}
            |""".stripMargin)
        )
        .check(
          status.is(201),
          jsonPath("$.email").is("${randomEmail}")
        )
        .check(bodyString.saveAs("responseBody")) // Add this line to save the response body
    )
    .exec(session => {
      println(session("responseBody").as[String]) // Print the response body
      session
    })
    .pause(2)


  //Setup

  setUp(
    postUsersScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
