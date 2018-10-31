package example.part_3_directives

import akka.http.scaladsl.server.{HttpApp, Route}

object IV_ComposingDirectives extends HttpApp {

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  def innerRoute(id: Int): Route = {
    concat(get {
      complete(s"get request for id: $id")
    }, post {
      complete(s"post request for id: $id")
    })
  }

  override protected def routes: Route = path("order" / IntNumber) { id =>
    innerRoute(id)
  }
}
