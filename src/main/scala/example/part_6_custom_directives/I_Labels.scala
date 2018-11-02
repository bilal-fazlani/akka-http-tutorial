package example.part_6_custom_directives

import akka.http.scaladsl.server.Route
import example.TMTApplication

/**
  * There are 3 ways to create custom directives
  * 1. Labeling
  * 2. Transform existing directives
  * 3. Write from scratch
  */
object I_Labels extends TMTApplication {

  override protected def routes: Route = path("products") {
    val getOrPost = get | post

    getOrPost {
      complete("OK")
    }
  }
}
