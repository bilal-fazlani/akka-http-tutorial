package example.part_5_authentication.V_OIDC_Authorization

import akka.http.scaladsl.server.Route
import example.part_5_authentication.V_OIDC_Authorization.Authentication._
import example.part_5_authentication.V_OIDC_Authorization.Authorization._
import example.{AkkaSystem, TMTApplication}

object V_OIDC_Authorization_App extends TMTApplication with AkkaSystem {

  override protected def routes: Route = Route.seal(
    authenticateOAuth2("master", authenticator) { accessToken =>
      post {
        authorize(hasPermission(accessToken, "write_data")) {
          complete("data write operation successful")
        }
      } ~
        get {
          authorize(hasPermission(accessToken, "read_data")) {
            complete("some protected data")
          }
        }
    }
  )
}
