package example

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}

import scala.util.{Random, Success}

object VIII_ServerBinding extends App with AkkaSystem {

  Http()
  //bind to localhost port 8080
  //this returns Source[IncomingConnection, Future[ServerBinding]]
    .bind("localhost", 8080)
    //add a printline hook
    .mapMaterializedValue(bindingFuture => {
      bindingFuture
        .onComplete {
          case Success(_) =>
            println("server started at port 8080")
          case scala.util.Failure(exception) =>
            println("could not bind port 8080")
            system.terminate()
        }
      bindingFuture
    })
    //handle every connection
    .runForeach(incomingConnection => {
      println(
        s"accepted new connected from ${incomingConnection.remoteAddress}")
      //handle the connection here
      incomingConnection handleWithSyncHandler handler
      //or
      //connection.handleWith(Flow[HttpRequest].map(handler))
    })

  //this handler handles every request and turns it into a response
  val handler: HttpRequest => HttpResponse = { req =>
    HttpResponse(entity = HttpEntity(Random.nextString(5)))
  }
}
