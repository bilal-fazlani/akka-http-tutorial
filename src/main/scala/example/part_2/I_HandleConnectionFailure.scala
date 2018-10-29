package example.part_2

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.IncomingConnection
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Flow, Sink}
import example.AkkaSystem

object I_HandleConnectionFailure extends App with AkkaSystem {
  val (host, port) = ("localhost", 8080)

  val logger = system.actorOf(MonitoringActor.props)

  val bindingF = Http()
    .bind(host, port)
    .mapMaterializedValue({ f =>
      f.onComplete { _ =>
        println(s"server started at $host:$port")
      }
      f
    })

  val reactToTopLevelFailures =
    Flow[IncomingConnection].watchTermination()((_, termination) => {
      termination.failed.foreach { cause =>
        {
          logger ! cause
        }
      }
    })

  val handleRequest = Flow[HttpRequest]
    .map(req => {
      println(s"accepted new request ${req.uri}")
      HttpResponse(entity = HttpEntity("OK"))
    })

  val handleConnections = Sink.foreach[IncomingConnection](con => {
    println(s"accepted new connection from ${con.remoteAddress}")
    con.handleWith(handleRequest)
  })

  val handleRequests =
    bindingF
      .via(reactToTopLevelFailures)
      .to(handleConnections)
      .run()
}

class MonitoringActor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("monitoring actor stated")
  }

  override def receive: Receive = {
    case ex: Exception => log.error(ex, "error has occurred somewhere")
  }
}
object MonitoringActor {
  def props = Props(classOf[MonitoringActor])
}
