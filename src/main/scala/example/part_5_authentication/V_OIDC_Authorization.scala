package example.part_5_authentication

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials.Provided
import example.{AkkaSystem, HttpApplication}
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}
import spray.json.{DefaultJsonProtocol, JsObject, RootJsonFormat}

import scala.util.Try

object V_OIDC_Authorization extends HttpApplication with AkkaSystem {

  case class AccessToken(sub: String,
                         name: String,
                         iat: Long,
                         permissions: Array[String])

  object AccessToken extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val accessTokenFormat: RootJsonFormat[AccessToken] = jsonFormat4(
      AccessToken.apply)
  }

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

  def hasPermission(accessToken: AccessToken, permission: String): Boolean = {
    accessToken.permissions.contains(permission)
  }

  override protected def routes: Route = Route.seal(
    authenticateOAuth2("master", authenticator) { accessToken =>
      post {
        authorize(hasPermission(accessToken, "write_data")) {
          complete("data write operation successful")
        }
      } ~
        get {
          authorize(hasPermission(accessToken, "read_data")) {
            complete(
              "data = AdNBAKDASJKHDKJASBDMN ASMNDKJASGDASJH Ajshdk KASHDJAHSDLKAS")
          }
        }
    }
  )
}
