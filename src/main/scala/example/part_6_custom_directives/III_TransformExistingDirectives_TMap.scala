package example.part_6_custom_directives

import akka.http.scaladsl.server.{Directive, Directive1, Route}
import example.HttpApplication

object III_TransformExistingDirectives_TMap extends HttpApplication {

  val twoInts: Directive[(Int, Int)] = parameter('int1.as[Int], 'int2.as[Int])

  val sumOfTwoInts: Directive[Tuple1[Int]] = twoInts.tmap {
    case (one, two) => one + two
  }

  override protected def routes: Route = path("products") {
    sumOfTwoInts { sum =>
      complete(sum.toString)
    }
  }
}
