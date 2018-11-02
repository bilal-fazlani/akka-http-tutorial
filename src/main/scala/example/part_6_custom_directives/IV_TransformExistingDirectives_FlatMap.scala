package example.part_6_custom_directives

import akka.http.javadsl.server.Rejections
import akka.http.scaladsl.server.{Directive1, Route}
import example.HttpApplication

object IV_TransformExistingDirectives_FlatMap extends HttpApplication {

  val idParam: Directive1[Int] = parameter('id.as[Int])

  val verifiedId: Directive1[Int] = idParam.flatMap {
    case id if id > 0 => provide(id * 2)
    case _ =>
      reject(Rejections.validationRejection("id can not be less than 0"))
  }

  override protected def routes: Route = path("products") {
    verifiedId { id =>
      complete(id.toString)
    }
  }
}
