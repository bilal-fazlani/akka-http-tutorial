package example.part_5_authentication

import akka.http.scaladsl.model.headers.{HttpChallenge, HttpCredentials}
import akka.http.scaladsl.server.Route
import example.{AkkaSystem, HttpApplication}

import scala.concurrent.Future

object II_authenticateOrRejectWithChallenge
    extends HttpApplication
    with AkkaSystem {

  def auth(cred: HttpCredentials): Boolean = {
    true
  }

  def userPassAuthenticator(credentials: Option[HttpCredentials])
    : Future[AuthenticationResult[String]] = {
    Future {
      credentials match {
        case Some(creds) if auth(creds) => Right(creds.token() + "SUCCESS")
        case _                          => Left(HttpChallenge("http", Some("master")))
      }
    }
  }

  override protected def routes: Route = Route.seal {
    path("secured") {
      authenticateOrRejectWithChallenge(userPassAuthenticator _) { username =>
        complete("Authenticated : " + username)
      }
    }
  }
}
