package example.part_1

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import example.WebServer

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
