package example.part_1

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import example.WebServer
import spray.json.DefaultJsonProtocol.{jsonFormat1, jsonFormat2}
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.duration.DurationDouble

object IV_ActorInteractions extends WebServer {

  case class Bid(userId: String, offer: Int)
  case object GetBids
  case class Bids(bids: List[Bid])

  class Auction extends Actor with ActorLogging {

    var bids: List[Bid] = List.empty

    override def receive: Receive = {
      case bid @ Bid(userId, offer) =>
        bids = bids :+ bid
        log.info(s"bid complete: $userId, $$$offer")
      case GetBids => sender() ! Bids(bids)
      case _       => log.info("Invalid message")
    }
  }

  import WebServer._

  implicit val bidFormat = jsonFormat2(Bid)
  implicit val bidsFormat = jsonFormat1(Bids)

  private val auctionRef: ActorRef =
    system.actorOf(Props[Auction], "auction-system")

  override val route: Route = path("auction") {
    put {
      parameter("bid".as[Int], "user") { (bid: Int, user: String) =>
        //place a bid, fire and forget
        auctionRef ! Bid(user, bid)
        complete(StatusCodes.Accepted, "bid placed")
      }
    } ~ get {
      implicit val timeout: Timeout = 5 seconds
      val bids: Future[Bids] = (auctionRef ? GetBids).mapTo[Bids]
      complete(bids)
    }
  }

  start()
}
