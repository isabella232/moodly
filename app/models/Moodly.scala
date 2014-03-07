package models

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

case class Moodly(id: String, start: Timestamp, intervalDays: Int)

class Moodlies(tag: Tag) extends Table[Moodly](tag, "MOODLY") {
  def id = column[String]("ID", O.PrimaryKey)

  def start = column[Timestamp]("START", O.NotNull)

  def intervalDays = column[Int]("INTERVAL_DAYS", O.NotNull)

  def * = (id, start, intervalDays) <> (Moodly.tupled, Moodly.unapply _)
}

object Moodlies {
  val moodlies = TableQuery[Moodlies]
}