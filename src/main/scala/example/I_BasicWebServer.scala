package example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object BasicWebServer extends WebServer {

  override val route: Route = path("hello") {
    get {
      complete("SUCCESS")
    }
  }

  startServer()
}
