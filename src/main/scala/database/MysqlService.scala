package database

import akka.actor.typed.scaladsl.Behaviors

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import slick.jdbc.MySQLProfile.api._

final case class Item(id: Option[Int], name: String, num: Int)
final case class Order(items: List[Item])

trait MysqlService extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val system = akka.actor.typed.ActorSystem(Behaviors.empty, "DatabaseExample")
  implicit val executionContext = system.executionContext
  // application.confから読み込み
  protected  val db = Database.forConfig("slick-mysql")
  // Json
  implicit val itemFormat = jsonFormat3(Item)
  implicit val orderFormat = jsonFormat1(Order)
  // Tableの仕様
  class Tests(tag: Tag) extends Table[Item](tag, "TEST") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def num = column[Int]("num")
    def * = (id.?, name, num) .<> (Item.tupled, Item.unapply)
  }
  // Tableを操作するのに使います
  val tests = TableQuery[Tests]
}
