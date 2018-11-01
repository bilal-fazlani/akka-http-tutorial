package example

import akka.http.scaladsl.server.HttpApp

abstract class HttpApplication extends HttpApp {

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }
}
