package example.part_6_custom_directives

import akka.http.scaladsl.server.Route
import example.TMTApplication

object VI_CustomAuthDirectiveExample extends TMTApplication {

  override protected def routes: Route =
    get {
      permission("read_data") {
        complete("OK")
      }
    } ~ post {
      permission("write_data") {
        complete("OK")
      }
    }
}
