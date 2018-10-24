package example

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.util.Random

object StreamRandomNumbers extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val numbers = Source.fromIterator(() => {
    Iterator.continually(Random.nextInt())
  })

  val route = path("random") {
    get {
      complete(
        HttpEntity(ContentTypes.`text/plain(UTF-8)`,
                   numbers.map(n => ByteString(s"$n\n")))
      )
    }
  }

  RouteUtil.serverRoutes(route)
}
