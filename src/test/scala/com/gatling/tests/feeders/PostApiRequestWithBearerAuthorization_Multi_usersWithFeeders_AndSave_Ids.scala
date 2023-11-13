package com.gatling.tests.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.nio.file.{Files, Paths, StandardOpenOption}
import scala.collection.mutable.ListBuffer

class PostApiRequestWithBearerAuthorization_Multi_usersWithFeeders_AndSave_Ids extends Simulation {

  private val token: String = "4a1bf4771cf69674c459eac33d3831575aa60a13eff438e007befcf3a489e721"

  // List buffer to store added user IDs
  val addedUserIds: ListBuffer[String] = ListBuffer()

  // protocol
  val httpProtocol = http
    .baseUrl("https://gorest.co.in/public/v2")
    .contentTypeHeader("application/json")
    .authorizationHeader(s"Bearer $token")

  // scenario
  val feeder = csv("data/dataToInject.csv").queue

  val feederScenario = scenario("Inject Multiple Users Data Using Feeders")
    .feed(feeder)
    .exec(session => {
      println("name : " + session("name").as[String])
      println("email :" + session("email").as[String])
      println("gender :" + session("gender").as[String])
      println("status :" + session("status").as[String])
      session
    })
    .exec(
      http("User Post Request using feeders and save ids to external file")
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
        .check(jsonPath("$.id").optional.saveAs("addedUserId")) // Save the added user ID

    )
    .exec(session => {
      val addedUserId = session("addedUserId").as[String]
      println(s"Added user with ID: $addedUserId")
      addedUserIds += addedUserId // Add the added user ID to the list buffer
      session
    })
    .pause(2)

  // setup
  setUp(feederScenario.inject(atOnceUsers(5))).protocols(httpProtocol)

  // cleanup
  after {
    // After the simulation, append the added user IDs to the CSV file
    val filePath = Paths.get(s"K:\\QaBoost\\performanceTesting\\Gatling\\Projects\\GatlingMavenProject\\src\\test\\resources\\data\\userIds.csv").toString
    println(s"Writing to file: $filePath") // Debugging
    try {
      Files.write(Paths.get(filePath), addedUserIds.mkString("\n").getBytes, StandardOpenOption.APPEND)
      println(s"Successfully wrote to file.")
    } catch {
      case e: Exception =>
        println(s"Error writing to file: ${e.getMessage}")
        e.printStackTrace() // Print the full stack trace for debugging
    }
  }
}
