package example.part_5_authentication

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials.{Missing, Provided}
import example.TMTApplication

object III_BearerTokenExtraction extends TMTApplication {

  val tokenExtractor: Authenticator[String] = {
    case Provided(id) => Some(id) // verify the token here
    case Missing      => None
  }

  override protected def routes: Route =
    authenticateOAuth2("master", tokenExtractor) { token =>
      complete(token)
    }
}
