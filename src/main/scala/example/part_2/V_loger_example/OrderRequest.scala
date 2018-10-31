package example.part_2.V_loger_example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.language.implicitConversions

case class OrderRequest(id: Option[Int], amount: Double, email: String) {
  def toOrder: Order = OrderRequest.toOrder(this)
}

object OrderRequest extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def toOrder(orderRequest: OrderRequest): Order = {
    if (orderRequest.id.nonEmpty) {
      Order(orderRequest.id.get, orderRequest.amount, orderRequest.email)
    } else {
      throw new RuntimeException("Order cant exist without an id")
    }
  }

  implicit val orderRequestFormat: RootJsonFormat[OrderRequest] =
    DefaultJsonProtocol.jsonFormat3(OrderRequest.apply)
}
