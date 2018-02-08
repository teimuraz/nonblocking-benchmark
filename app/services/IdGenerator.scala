package services

import scala.concurrent.Future

trait IdGenerator {
  def nextId: Future[Long]
}
