package controllers

import java.util.Random
import javax.inject._

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._
import services.IdGenerator
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()
    (idGenerator: IdGenerator,
     cc: ControllerComponents,
     dbConfigProvider: DatabaseConfigProvider,
     wsClient: WSClient)
    (implicit ec: ExecutionContext) extends AbstractController(cc) {

  protected val dbConfig = dbConfigProvider.get[JdbcProfile]
  def db = dbConfig.db

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def parallel = Action.async {
    val t0 = System.nanoTime()

    val id1 = idGenerator.nextId
    val id2 = idGenerator.nextId
    val id3 = idGenerator.nextId
    val id4 = idGenerator.nextId
    val id5 = idGenerator.nextId
    val id6 = idGenerator.nextId
    val id7 = idGenerator.nextId
    val id8 = idGenerator.nextId
    val id9 = idGenerator.nextId
    val id10 = idGenerator.nextId
    for {
      i1 <- id1
      i2 <- id2
      i3 <- id3
      i4 <- id4
      i5 <- id5
      i6 <- id6
      i7 <- id7
      i8 <- id8
      i9 <- id9
      i10 <- id10
    } yield {
      val t1 = System.nanoTime()
      val dt = "Elapsed time: " + (t1 - t0) + " ns"
       Ok(s"Done. $dt")
    }
  }

  def sequential = Action.async {
    val t0 = System.nanoTime()
    for {
      i1 <- idGenerator.nextId
      i2 <- idGenerator.nextId
      i3 <- idGenerator.nextId
      i4 <- idGenerator.nextId
      i5 <- idGenerator.nextId
    } yield {
      val t1 = System.nanoTime()
      val dt = "Elapsed time: " + (t1 - t0) + " ns"
      Ok(s"Done. $dt")
    }
  }

  def remoteCallPar = Action.async {
    val t0 = System.nanoTime()
    val rand = new Random()
    rand.nextInt(100) + 1
    val r1 = wsClient.url("https://www.emoney.ge/index.php/main/welcome?"+rand.nextInt(1000) + 1).get()
    val r2 = wsClient.url("https://www.emoney.ge/index.php/main/welcome?"+rand.nextInt(1000) + 1).get()
    val r3 = wsClient.url("https://www.emoney.ge/index.php/main/welcome?"+rand.nextInt(1000) + 1).get()
    for {
      s1 <- r1
      s2 <- r2
      s3 <- r3
    } yield {
//      val r = s1.body + s2.body + s3.body
//      Ok(r)
      val t1 = System.nanoTime()
      val dt = "Elapsed time: " + (t1 - t0) + " ns"
      Ok(s"Done. $dt")
    }
  }

  def complexQuery = Action.async {
    val t0 = System.nanoTime()
    val rand = new Random()
    rand.nextInt(100) + 1
    val qf1 = runComplexQuery
    val qf2 = runComplexQuery
    val qf3 = runComplexQuery
    for {
      q1 <- qf1
      q2 <- qf2
      q3 <- qf3
    } yield {
      val t1 = System.nanoTime()
      val dt = "Elapsed time: " + (t1 - t0) + " ns"
      Ok(s"Done. $dt $q1 $q2 $q3")
    }
  }

  private def runComplexQuery = {
    val sql = sql"""SELECT _accession_key, _refs_key FROM mgd.acc_accessionreference TABLESAMPLE SYSTEM (0.01)""".as[(Int, Int)]
    val future: Future[Any] = db.run(sql).map(_.head)
    future
  }
}
