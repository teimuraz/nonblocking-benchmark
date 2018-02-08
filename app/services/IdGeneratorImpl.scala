package services
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._


@Singleton
class IdGeneratorImpl @Inject()
    (val dbConfigProvider: DatabaseConfigProvider)
   (implicit ec: ExecutionContext) extends IdGenerator {

  protected val dbConfig = dbConfigProvider.get[JdbcProfile]
  def db = dbConfig.db

  override def nextId: Future[Long] = {
    val sql = sql"""select nextval('scala_id_seq')""".as[Long]
    db.run(sql).map(_.head)
  }
}
