package example.part_6_custom_directives

import akka.http.scaladsl.server.{Directive, Directive1, Route}
import example.HttpApplication

object II_TransformExistingDirectives_Map extends HttpApplication {

  val numberParam: Directive1[Int] = parameter('number.as[Int])

  val squareParam: Directive[Tuple1[Int]] = numberParam.map { n =>
    n * n
  }

  override protected def routes: Route = path("products") {
    squareParam { number =>
      complete(number.toString)
    }
  }
}
