package models

import play.api.db.slick.Config.driver.simple._

case class Ballot(id: Long, moodlyId: String, cookieId: String, iterationCount: Int, vote: Int)

class Ballots(tag: Tag) extends Table[Ballot](tag, "BALLOT") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def moodlyId = column[String]("MOODLY_ID", O.NotNull)

  def cookieId = column[String]("COOKIE_ID", O.NotNull)

  def iterationCount = column[Int]("ITERATION_COUNT", O.NotNull)

  def vote = column[Int]("VOTE", O.NotNull)

  def * = (id, moodlyId, cookieId, iterationCount, vote) <>(Ballot.tupled, Ballot.unapply _)

  def moodly = foreignKey("MOOLY_FK", moodlyId, Moodlies.moodlies)(_.id)
}

object Ballots {
  def ballots = TableQuery[Ballots]

  def insert(ballot: Ballot)(implicit s: Session): Long = {
    ballots.returning(ballots.map(_.id)).insert(ballot)
  }
}
