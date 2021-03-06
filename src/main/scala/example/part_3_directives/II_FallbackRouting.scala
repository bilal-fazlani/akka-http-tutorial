package example.part_3_directives

import akka.http.scaladsl.server.Route
import example.TMTApplication

object II_FallbackRouting extends TMTApplication {
  override protected def routes: Route =
    get {
      complete("GET REQUEST")
    } ~ post { ctx =>
      ctx.complete("POST") // ctx can be available here if you want. should not need this
    } ~ complete("SOMETHING ELSE")
}
