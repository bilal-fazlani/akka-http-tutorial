package example

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object MarshallerWebServer extends App {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  final case class Item(name: String, id: Long)

  final case class Order(items: List[Item])

  var allItems: List[Item] = Nil

  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat2(Item)
  implicit val orderFormat: RootJsonFormat[Order] = jsonFormat1(Order)

  def fetchItem(itemId: Long): Future[Option[Item]] =
    Future(allItems.find(_.id == itemId))

  def saveOrder(order: Order): Future[Done] = {
    allItems = order match {
      case Order(items) => items ::: allItems
      case _            => allItems
    }
    Future {
      Done
    }
  }

  val route: Route = {
    get {
      path("items") {
        complete(allItems)
      } ~ pathPrefix("item" / LongNumber) { itemId =>
        val eventullayMaybeItem: Future[Option[Item]] = fetchItem(itemId)

        onSuccess(eventullayMaybeItem) {
          case Some(item) => complete(item)
          case None       => complete(StatusCodes.NotFound)
        }
      }
    } ~ post {
      path("item") {
        entity(as[Order]) { order =>
          val saved: Future[Done] = saveOrder(order)
          onSuccess(saved) { _ =>
            complete(StatusCodes.Created, "Order created")
          }
        }
      }
    }
  }

  RouteUtil.serverRoutes(route)
}
