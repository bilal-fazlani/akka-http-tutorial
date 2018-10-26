package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.util.{Failure, Success}

object VI_HttpClient extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val routes: Route = path("person") {
    get {
      complete("~~~~~~~~~~~~~~~~~~")
    }
  }

  Http()
    .bindAndHandle(routes, "localhost", 8080)
    .onComplete {
      case Success(sb) => {
        println("server started on port http://localhost:8080")

        println("making a request to /person")

        Http()
          .singleRequest(HttpRequest(GET, Uri("http://localhost:8080/person")))
          .onComplete({
            case Success(httpResponse) =>
              println("success")
              println(httpResponse)
            case Failure(exception) =>
              sys.error("error")
              println(exception)
          })

        StdIn.readLine()
        sb.unbind().onComplete(_ => system.terminate())
      }
      case Failure(exception) => println(exception)
    }

}
