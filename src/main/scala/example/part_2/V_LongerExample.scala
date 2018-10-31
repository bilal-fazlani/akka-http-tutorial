package example.part_2

import akka.http.scaladsl.coding.{Deflate, Gzip}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{HttpApp, Route}
import example.part_2.V_loger_example._

object V_LongerExample extends HttpApp {

  def main(args: Array[String]): Unit = {
    startServer("localhost", 8080)
  }

  val routes: Route =
    logRequest("request") {
      encodeResponseWith(Gzip) { //You can also use Deflate here
        decodeRequest {
          //GET ALL ORDERS
          path("orders") {
            get {
              complete(Order.data)
            } ~
              // CREATE/UPDATE NEW ORDER
              post {
                entity(as[OrderRequest]) { order =>
                  if (order.id.isEmpty) { //CREATE
                    val lastId = Order.data.maxBy(_.id).id
                    Order.data = Order.data :+ order
                      .copy(id = Some(lastId + 1))
                      .toOrder
                    complete(StatusCodes.Created)
                  } else { //UPDATE
                    val targetOrder = Order.data.find(_.id == order.id.get)
                    if (targetOrder.nonEmpty) {
                      Order.data =
                        Order.data.filter(_.id != targetOrder.get.id) :+ order.toOrder
                          .copy(id = targetOrder.get.id)

                      complete(StatusCodes.OK)
                    } else { //NOT FOUND
                      complete(StatusCodes.NotFound)
                    }
                  }
                }
              }
          } ~
            // GET ONE ORDER
            pathPrefix("orders" / IntNumber) { id =>
              pathEndOrSingleSlash {
                val data = Order.data.find(_.id == id)

                if (data.nonEmpty)
                  complete(data.head)
                else
                  complete(StatusCodes.NotFound)
              }
            } ~
            path("oldApi" / Remaining) { pathRest =>
              redirect(s"http://localhost:9090/$pathRest",
                       StatusCodes.MovedPermanently)
            }
        }
      }
    }

}
