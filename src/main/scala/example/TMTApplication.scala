package example

import akka.http.scaladsl.server.{Directive0, HttpApp}
import example.part_5_authentication.V_OIDC_Authorization.{
  Authentication,
  Authorization
}

abstract class TMTApplication extends HttpApp {

  def permission(name: String): Directive0 =
    authenticateOAuth2("master", Authentication.authenticator).flatMap { at =>
      authorize(Authorization.hasPermission(at, name))
    }

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }
}
