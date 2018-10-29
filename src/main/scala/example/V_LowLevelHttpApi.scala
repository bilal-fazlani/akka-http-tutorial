package example

import akka.http.scaladsl.model._
import WebServer._

object V_LowLevelHttpApi extends App {

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, Uri.Path("/resource"), _, _, _) =>
      HttpResponse(StatusCodes.OK)
    case HttpRequest(HttpMethods.POST, Uri.Path("/resource"), _, _, _) =>
      HttpResponse(StatusCodes.Created)
    case r: HttpRequest =>
      r.discardEntityBytes()
      HttpResponse(StatusCodes.NotFound)
  }

  WebServer.start(requestHandler, 8080)
}