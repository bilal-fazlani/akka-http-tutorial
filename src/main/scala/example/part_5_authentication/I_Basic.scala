package example.part_5_authentication

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials.Provided
import example.HttpApplication

object I_Basic extends HttpApplication {

  val authenticator: AuthenticatorPF[User] = {
    case p @ Provided(id) if p.verify("psd") => User(id)
  }

  override protected def routes: Route = Route.seal(
    authenticateBasicPF("master", authenticator) { user =>
      {
        complete(s"SUCCESS for $user")
      }
    }
  )

  case class User(name: String)
}
