package example.part_3_directives

import akka.http.scaladsl.server.Route
import example.TMTApplication

object IV_ComposingDirectives extends TMTApplication {

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
