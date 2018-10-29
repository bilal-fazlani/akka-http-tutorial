package example

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}

import scala.util.Random

object VIII_ServerBinding extends App with AkkaSystem {

  Http()
  //bind to localhost port 8080
  //this returns Source[IncomingConnection, Future[ServerBinding]]
    .bind("localhost", 8080)
    //add a printline hook
    .mapMaterializedValue(bindingFuture => {
      bindingFuture.onComplete(_ => println("server started..."))
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
