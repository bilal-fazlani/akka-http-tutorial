package example.part_5_authentication

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import example.HttpApplication

object I_Basic extends HttpApplication {

  override protected def routes: Route =
    authenticateBasicPF("master", {
      case c @ Credentials.Provided(userName) if c.verify("password!") =>
        Some(userName)
    }) { userName =>
      path("secure") {
        complete(s"logged in username is ${userName.get}")
      }
    } ~
      path("public") {
        complete("public")
      }
}
