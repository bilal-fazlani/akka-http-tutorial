package example.part_4_directives2

import akka.http.scaladsl.server.Route
import example.TMTApplication
object I_Header extends TMTApplication {

  override protected def routes: Route = headerValueByName("host") { host =>
    complete(s"the host & port was $host")
  }
}
