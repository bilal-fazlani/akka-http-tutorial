package example.part_3_directives

import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.server.{Directive, Route}
import example.TMTApplication

object III_MixingDirectives extends TMTApplication {

  val getOrPost: Directive[Unit] = get | post

  val getOrPostWithMethod
    : Directive[Tuple1[HttpMethod]] = getOrPost & extractMethod

  val finalRoute
    : Directive[(Int, HttpMethod)] = path("order" / IntNumber) & getOrPostWithMethod

  override protected def routes: Route =
    finalRoute { (orderId, method) =>
      complete(s"$method method for order: $orderId")
    }

  //alternative readable route
  val readableRoute: Route = (get | put) {
    extractMethod { m =>
      path("order" / IntNumber) { orderId =>
        complete(s"$m method for order $orderId")
      }
    }
  }
}
