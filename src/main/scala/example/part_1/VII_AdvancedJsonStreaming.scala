package example.part_1

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import example.AkkaSystem

object VII_AdvancedJsonStreaming extends App with AkkaSystem {
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

  import MyJsonProtocol._

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport =
    EntityStreamingSupport.json()

  val httpResponse =
    HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, input))

  httpResponse.entity.dataBytes
    .via(jsonStreamingSupport.framingDecoder) // pick your Framing (could be "\n" etc)
    .mapAsync(1) { byteString =>
      Unmarshal(byteString).to[Tweet]
    }
    .runForeach(println(_))
    .onComplete(_ => system.terminate())
}
