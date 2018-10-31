package example.part_2.V_loger_example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Order(id: Int, amount: Double, email: String)

object Order extends SprayJsonSupport with DefaultJsonProtocol {
  var data =
    Seq(
      Order(1, 34.67, "abc@pqr.com"),
      Order(2, 45.3, "mb@kgh.com")
    )

  implicit val orderFormat: RootJsonFormat[Order] =
    DefaultJsonProtocol.jsonFormat3(Order.apply)
}
