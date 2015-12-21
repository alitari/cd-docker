package accept1

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  val baseUrl = System.getProperty("baseUrl")
  
  val httpConf = http
    .baseURL(baseUrl) // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> """text/xml;charset=ISO-8859-1""") // Note the headers specific to a given request

  
  val scn1 = scenario("Scenario1") // A scenario is a chain of requests and pauses
    .exec(http("request_11")
      .post("/wildfly-webservice").headers(headers_10)
      .body(StringBody("""
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:hel="http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld">
           <soapenv:Header/>
           <soapenv:Body>
             <hel:sayHello/>
           </soapenv:Body>"""))
      .check(status.is(200))
      .check(bodyString.is("""<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns2:sayHelloResponse xmlns:ns2="http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld"><return>Hello World!</return></ns2:sayHelloResponse></soap:Body></soap:Envelope>""")))
  
      
      
   val scn2 = scenario("Scenario2") 
    .exec(http("request_21")
      .post("/wildfly-webservice").headers(headers_10)
      .body(StringBody("""
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:hel="http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld">
           <soapenv:Header/>
           <soapenv:Body>
             <hel:sayHello/>
           </soapenv:Body>"""))
      .check(status.is(200))
      .check(bodyString.is("""<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns2:sayHelloResponse xmlns:ns2="http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld"><return>Hello World!</return></ns2:sayHelloResponse></soap:Body></soap:Envelope>""")))
  
      setUp(scn1.inject(atOnceUsers(5)).protocols(httpConf),
            scn2.inject(rampUsers(500) over (20 ) ).protocols(httpConf))
            .assertions(global.successfulRequests.percent.is(100),
                        global.responseTime.max.lessThan(500))
}