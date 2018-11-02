package example.part_6_custom_directives

import akka.http.javadsl.server.Rejections
import akka.http.scaladsl.server.{Directive0, Route}
import example.HttpApplication

object VI_CustomAuthDirectiveExample extends HttpApplication {

  import example.part_5_authentication.V_OIDC_Authorization._

  def myCustomAuth_Approach1(permission: String): Directive0 =
    authenticateOAuth2("master", Authentication.authenticator)
      .require({ at =>
        Authorization.hasPermission(at, permission)
      }, Rejections.authorizationFailed)

  /**
    *approach 2
    */
  def TMTAuth(permission: String): Directive0 =
    authenticateOAuth2("master", Authentication.authenticator).flatMap { at =>
      authorize(Authorization.hasPermission(at, permission))
    }

  override protected def routes: Route =
    get {
      TMTAuth("read_data") {
        complete("OK")
      }
    } ~ post {
      TMTAuth("write_data") {
        complete("OK")
      }
    }
}
