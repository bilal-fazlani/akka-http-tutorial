package example.part_2

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.IncomingConnection
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Flow, Sink}
import example.AkkaSystem

object II_ConnectionFailure extends App with AkkaSystem {
  val (host, port) = ("localhost", 8080)

  val bindingSource = Http()
    .bind(host, port)
    .mapMaterializedValue(bindingF => {
      bindingF.onComplete {
        case scala.util.Success(_) => println("server started at 8080")
        case scala.util.Failure(_) =>
          println("could not bind")
          system.terminate()
      }
      bindingF
    })

  val handleConnectionFailure = Flow[HttpRequest].recover {
    case ex: Exception =>
      println(ex)
      throw ex
  }

  val echo = Flow[HttpRequest].map { req =>
    {
      HttpResponse(
        entity = HttpEntity(req.entity.contentType, req.entity.dataBytes))
    }
  }

  val sink = Sink.foreach[IncomingConnection] { con =>
    con.handleWith(handleConnectionFailure.via(echo))
  }

  bindingSource
    .to(sink)
    .run()
}
