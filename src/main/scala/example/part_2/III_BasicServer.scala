package example.part_2

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import example.AkkaSystem

import scala.io.StdIn
import scala.util.Success

object III_BasicServer extends App with AkkaSystem {
  val route = path("hello") {
    get {
      println("request received!")
      complete("<h1>Hi, User</h1>")
    }
  }

  val (host, port) = ("localhost", 8080)

  val bindingF = Http()
    .bindAndHandle(route, host, port)
    .andThen({
      case Success(_) => println("server online at port 8080")
      case scala.util.Failure(exception) =>
        println(exception)
        system.terminate()
    })

  StdIn.readLine()

  bindingF
    .flatMap(b => b.unbind())
    .onComplete(_ => system.terminate())
}
