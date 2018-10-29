package example

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future

object VII_JsonStreaming extends App {
  val input: String =
    """{"uid":1, "txt":"akka rocks"},
      |{"uid":2, "txt":"streaming is so hot right now"},
      |{"uid":3, "txt":"you cannot enter the same river twice"}
    """.stripMargin

  import akka.http.scaladsl.common.{
    EntityStreamingSupport,
    JsonEntityStreamingSupport
  }
  import akka.http.scaladsl.unmarshalling._

  case class Tweet(uid: Int, txt: String)
  object MyJsonProtocol
      extends akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
      with spray.json.DefaultJsonProtocol {

    implicit val tweetFormat = jsonFormat2(Tweet.apply)
  }

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()

  val httpResponse =
    HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, input))

  implicit val actorSystem = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  import MyJsonProtocol._
  //unmarshal
  val unmarshalled = Unmarshal(httpResponse).to[Source[Tweet, NotUsed]]

  //flatten Future[Source[T]] to Source[T]
  val source: Source[Tweet, Future[NotUsed]] =
    Source.fromFutureSource(unmarshalled)

  source
    .runForeach(t => println("----" + t))
    .onComplete(_ => actorSystem.terminate())

}
