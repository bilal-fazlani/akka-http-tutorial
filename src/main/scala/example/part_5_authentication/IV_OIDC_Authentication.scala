package example.part_5_authentication

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials.Provided
import example.HttpApplication
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import spray.json.JsObject

object IV_OIDC_Authentication extends HttpApplication {

  val authenticator: Authenticator[JsObject] = {
    case p @ Provided(token) => {
      val obj =
        JwtSprayJson.decodeJson(token, "P@ssW0rd!123", Seq(JwtAlgorithm.HS256))
      obj match {
        case scala.util.Success(value) => Some(value)
        case _                         => None
      }
    }
    case _ => None
  }

  override protected def routes: Route = Route.seal(
    authenticateOAuth2("master", authenticator) { obj =>
      complete(obj)
    }
  )
}
