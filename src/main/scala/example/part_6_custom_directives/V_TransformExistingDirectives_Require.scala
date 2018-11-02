package example.part_6_custom_directives

import akka.http.javadsl.server.Rejections
import akka.http.scaladsl.server.{Directive0, Route}
import example.HttpApplication

object V_TransformExistingDirectives_Require extends HttpApplication {

  val localRequestsOnly: Directive0 =
    extractHost.require(h => {
      println(h)
      h == "localhost"
    }, Rejections.authorizationFailed)

  override protected def routes: Route = path("products") {
    localRequestsOnly {
      complete("OK")
    }
  }
}
