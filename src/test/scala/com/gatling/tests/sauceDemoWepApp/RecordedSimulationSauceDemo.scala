package com.gatling.tests.sauceDemoWepApp

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.concurrent.duration._

class RecordedSimulationSauceDemo extends Simulation {

	val httpProtocol = http
		.baseUrl("https://www.saucedemo.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")



	val sauceDemoScenario = scenario("RecordedSimulationSauceDemo")
		.exec(http("HomePage_SauceDemo")
			.get("/v1/index.html")
			)
		.pause(21)
		.exec(http("Inventory")
			.get("/v1/inventory.html")
			)
		.pause(14)
		.exec(http("CartPage")
			.get("/v1/cart.html")
			)
		.pause(15)
		.exec(http("Checkout1")
			.get("/v1/checkout-step-one.html")
			)
		.pause(14)
		.exec(http("Checkout2")
			.get("/v1/checkout-step-two.html")
			)
		.pause(9)
		.exec(http("CompleteCheckout")
			.get("/v1/checkout-complete.html")
			)
		.pause(18)
		.exec(http("Logout")
			.get("/v1/index.html")
			)

	setUp(sauceDemoScenario.inject(rampUsers(20).during(30))).protocols(httpProtocol)
}