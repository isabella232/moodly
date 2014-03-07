package models

import play.api.db.slick.Config.driver.simple._

case class Ballot(id: Int, moodlyId: String, cookieId: String, iterationCount: Int, vote: Int)

class Ballots(tag: Tag) extends Table[Ballot](tag, "BALLOT") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def moodlyId = column[String]("MOODLY_ID", O.NotNull)

  def cookieId = column[String]("COOKIE_ID", O.NotNull)

  def iterationCount = column[Int]("ITERATION_COUNT", O.NotNull)

  def vote = column[Int]("VOTE", O.NotNull)

  def * = (id, moodlyId, cookieId, iterationCount, vote) <>(Ballot.tupled, Ballot.unapply _)
}

object Ballots {
  def ballots = TableQuery[Ballots]
}
