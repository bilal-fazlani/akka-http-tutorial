package example.part_2

import akka.actor.{Actor, ActorLogging, Props}

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
