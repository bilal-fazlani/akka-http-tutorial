package example

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.util.Random

object III_StreamRandomNumbers extends WebServer {

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

  start()
}
