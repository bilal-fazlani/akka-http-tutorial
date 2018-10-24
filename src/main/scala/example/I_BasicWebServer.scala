package example

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

object BasicWebServer extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val route: Route = path("hello") {
    get {
      complete("SUCCESS")
    }
  }

  RouteUtil.serverRoutes(route)
}
