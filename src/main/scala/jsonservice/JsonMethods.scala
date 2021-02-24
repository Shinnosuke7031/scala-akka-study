package jsonmethods

import akka.Done
import scala.concurrent.Future

import database._
import slick.jdbc.MySQLProfile.api._

trait JsonMethods extends MysqlService {

  def fetchItem(itemNum: Int): Future[Option[Item]] = db.run(
    tests.filter(_.num === itemNum).result.headOption
  )

  def saveOrder(order: Order): Future[Done] = {
    order.items.foreach(item => item match {
      case Item(id, name, num) => db.run(TestsTableAutoInc += item)
      case _ => None
    })
    
    Future { Done }
  }
  // AutoIncrementするために必要
  protected def TestsTableAutoInc = tests returning tests.map(_.id)
}