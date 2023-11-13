package com.gatling.tests.sauceDemoWepApp

import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*

import scala.concurrent.duration.*

class RecordedSimulationSauceDemo_IsolatedModule extends Simulation {

	val httpProtocol = http
		.baseUrl("https://www.saucedemo.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")

	val homePage = exec(http("HomePage_SauceDemo")
		.get("/v1/index.html")
	)
		.pause(21)

	val inventaire = exec(http("Inventory")
		 .get("/v1/inventory.html")
	 )
		 .pause(14)
	val cartPage= exec(http("CartPage")
		 .get("/v1/cart.html")
	 )
		 .pause(15)

val firstCheckout= exec(http("Checkout1")
	.get("/v1/checkout-step-one.html")
)
	.pause(14)

val secondCheckout = exec(http("Checkout2")
	.get("/v1/checkout-step-two.html")
)
	.pause(9)
val completeCheckout = exec(http("CompleteCheckout")
	.get("/v1/checkout-complete.html")
)
	.pause(18)
val logOut = exec(http("Logout")
	.get("/v1/index.html")
)
	val sauceDemoIsolatedScenario = scenario("RecordedSimulationIsolatedSauceDemo")
		.exec(homePage,inventaire,cartPage,firstCheckout,secondCheckout,completeCheckout,logOut)

	/*********************************************************************************************************/
//add  users role to execute scenarions
val users = scenario("users")
	.exec(homePage)

	val admins = scenario("admins")
		.exec(homePage, inventaire, cartPage, firstCheckout, secondCheckout, completeCheckout, logOut)

  //generate 50 users in 30 seconds and generate 3 admins users in 15 seconds
	setUp(
		users.inject(rampUsers(50).during(30)),
		admins.inject(rampUsers(3).during(15))
		).protocols(httpProtocol)
/*********************************************************************************************************/

	// setUp(sauceDemoIsolatedScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
}