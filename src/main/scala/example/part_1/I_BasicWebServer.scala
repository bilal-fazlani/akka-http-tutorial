package example.part_1

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import example.WebServer

object I_BasicWebServer extends WebServer {

  override val route: Route = path("hello") {
    get {
      complete("SUCCESS")
    }
  }

  start()
}
