package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.reflect.ClassTag

abstract class WebServer extends App {
  val route: Route
  val port: Int = 8080

  def start() =
    WebServer.start(route, port)
}

object WebServer extends AkkaSystem {

  private val localhost = "localhost"

  def start[X: ClassTag](route: Route, port: Int): Unit = {
    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandle(route, localhost, port)

    bind(port, bindingFuture)
  }

  def start[X: ClassTag, Y: ClassTag](handler: HttpRequest => HttpResponse,
                                      port: Int): Unit = {
    val bindingFuture: Future[Http.ServerBinding] =
      Http().bindAndHandleSync(handler, localhost, port = port)

    bind(port, bindingFuture)
  }

  private def bind(port: Int, bindingFuture: Future[Http.ServerBinding])(
      implicit system: ActorSystem,
      ec: ExecutionContext) = {
    println(s"server online at http://$localhost:$port")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
