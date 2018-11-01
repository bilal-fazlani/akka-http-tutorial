package example.part_5_authentication.V_OIDC_Authorization

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class AccessToken(sub: String,
                       name: String,
                       iat: Long,
                       permissions: Array[String])

object AccessToken extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val accessTokenFormat: RootJsonFormat[AccessToken] = jsonFormat4(
    AccessToken.apply)
}
