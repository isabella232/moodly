package models

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
import tyrex.services.UUID

case class Moodly(id: String, start: Timestamp, intervalDays: Int) {
  def this(intervalDays: Int) = this(UUID.create(), new Timestamp(System.currentTimeMillis()), intervalDays)
}

class Moodlies(tag: Tag) extends Table[Moodly](tag, "MOODLY") {
  def id = column[String]("ID", O.PrimaryKey)

  def start = column[Timestamp]("START", O.NotNull)

  def intervalDays = column[Int]("INTERVAL_DAYS", O.NotNull)

  def * = (id, start, intervalDays) <>(Moodly.tupled, Moodly.unapply _)
}

object Moodlies {
  val moodlies = TableQuery[Moodlies]

  def insert(moodly: Moodly)(implicit s: Session) {
    moodlies.insert(moodly)
  }
}