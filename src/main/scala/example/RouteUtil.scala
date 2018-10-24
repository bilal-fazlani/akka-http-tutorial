package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object RouteUtil {
  def serverRoutes(route: Route)(implicit system: ActorSystem,
                                 materializer: ActorMaterializer,
                                 ec: ExecutionContext): Unit = {
    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle(route, "localhost", 8080)

    println("server online at http://localhost:8080")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
