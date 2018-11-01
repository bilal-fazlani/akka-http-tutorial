package example.part_5_authentication.V_OIDC_Authorization

import akka.http.scaladsl.server.directives.Credentials.Provided
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import spray.json.JsObject
import akka.http.scaladsl.server.Directives._

import scala.util.Try

object Authentication {
  val authenticator: Authenticator[AccessToken] = {
    case p @ Provided(token) => {
      val obj: Try[JsObject] =
        JwtSprayJson.decodeJson(token, "P@ssW0rd!123", Seq(JwtAlgorithm.HS256))

      obj match {
        case scala.util.Success(v) => {
          val at: AccessToken = AccessToken.accessTokenFormat.read(v)
          Some(at)
        }
        case _ => None
      }
    }
    case _ => None
  }
}
