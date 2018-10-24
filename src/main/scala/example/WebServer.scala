package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

abstract class WebServer extends App {
  val route: Route
  val port: Int = 8080

  implicit val system = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  def startServer(): Unit = {
    require(route != null, "please define routes")

    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle(route, "localhost", port)

    println(s"server online at http://localhost:$port")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
