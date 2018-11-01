package example.part_3_directives

import akka.http.scaladsl.server.Route
import example.HttpApplication

object I_Introduction extends HttpApplication {

  val oneTime: Route = {
    println("MARK1")
    ctx =>
      ctx.complete("yeah!")
  }

  val everyTime: Route = { ctx =>
    println("MARK2")
    ctx.complete("yeah!")
  }

  //USING COMPLETE DIRECTIVE

  val oneTimeUsingCompleteDirective: Route = {
    println("MARK3")
    complete("yeah!")
  }

  val everyTimeUsingCompleteDirective: Route = complete {
    println("MARK4")
    "yeah!"
  }

  override protected def routes: Route = everyTimeUsingCompleteDirective
}
