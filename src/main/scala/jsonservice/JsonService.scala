package jsonservice

import akka.http.scaladsl.server.Directives
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.Future

import jsonmethods.JsonMethods
import database.{Item, Order}

trait JsonService extends Directives with JsonMethods {

  val route: Route =
    concat(
      get {
        pathPrefix("item" / IntNumber) { num =>
          val maybeItem: Future[Option[Item]] = fetchItem(num)
          onSuccess(maybeItem) {
            case Some(item) => {complete(item)}
            case None       => complete(StatusCodes.NotFound)
          }
        }
      },
      post {
        path("create-order") {
          entity(as[Order]) { order =>
            val saved: Future[Done] = saveOrder(order)
            onSuccess(saved) { _ => {
              complete("saved completely")
            }}
          }
        }
      }
    )
}