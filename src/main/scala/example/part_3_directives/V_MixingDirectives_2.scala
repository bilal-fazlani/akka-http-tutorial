package example.part_3_directives

import akka.http.scaladsl.server.Route
import example.TMTApplication

object V_MixingDirectives_2 extends TMTApplication {

  //important to not when you AND directives their extractions for a tuple
  val orderRoute = path("order" / IntNumber) & parameters('oem, 'expired ?)
  override protected def routes: Route = orderRoute { (orderId, oem, expired) =>
    {
      println((orderId, oem, expired))
      complete("OK")
    }
  }

  //important to note its possible to OR them because they
  //both extract the same the signature-> tuple(Int)
  val anotherExample = path("order" / IntNumber) | parameters('orderId.as[Int])
  val anotherRoute = anotherExample { orderId =>
    println(orderId)
    complete("OK")
  }
}
