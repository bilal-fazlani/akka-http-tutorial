package example.part_2

import akka.http.scaladsl.server.{HttpApp, Route}

object IV_HttpApp extends HttpApp {

  val routes: Route = path("hello") {
    get {
      complete("OK")
    }
  }

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }
}
