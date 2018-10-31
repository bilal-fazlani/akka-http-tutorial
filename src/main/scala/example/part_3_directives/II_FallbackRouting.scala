package example.part_3_directives

import akka.http.scaladsl.server.{HttpApp, Route}

object II_FallbackRouting extends HttpApp {
  override protected def routes: Route =
    get {
      complete("GET REQUEST")
    } ~ post { ctx =>
      ctx.complete("POST") // ctx can be available here if you want. should not need this
    } ~ complete("SOMETHING ELSE")

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }
}
