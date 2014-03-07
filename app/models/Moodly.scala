package models

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
import tyrex.services.UUID
import play.api.libs.json.{JsNumber, JsString, JsObject, Writes}

case class Moodly(id: String, start: Timestamp, intervalDays: Int) {
  def this(intervalDays: Int) = this(UUID.create(), new Timestamp(System.currentTimeMillis()), intervalDays)
}

object Moodly {

  implicit object MoodlyWrites extends Writes[Moodly] {
    override def writes(moodly: Moodly) = JsObject(Seq(
      "id" -> JsString(moodly.id),
      "start" -> JsNumber(moodly.start.getTime),
      "intervalDays" -> JsNumber(moodly.intervalDays)))
  }

}

class Moodlies(tag: Tag) extends Table[Moodly](tag, "MOODLY") {
  def id = column[String]("ID", O.PrimaryKey)

  def start = column[Timestamp]("START", O.NotNull)

  def intervalDays = column[Int]("INTERVAL_DAYS", O.NotNull)

  def * = (id, start, intervalDays) <>( {
    row: (String, Timestamp, Int) => Moodly(row._1, row._2, row._3)
  }, Moodly.unapply _)
}

object Moodlies {
  val moodlies = TableQuery[Moodlies]

  def insert(moodly: Moodly)(implicit s: Session) {
    moodlies.insert(moodly)
  }
}