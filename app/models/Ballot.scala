package models

import play.api.db.slick.Config.driver.simple._
import play.api.libs.json.{JsNumber, JsString, JsObject, Writes}

case class Ballot(id: Long, moodlyId: String, cookieId: String, iterationCount: Int, vote: Int)

object Ballot {

  implicit object BallotWrites extends Writes[Ballot] {
    override def writes(ballot: Ballot) = JsObject(Seq(
      "id" -> JsNumber(ballot.id),
      "moodlyId" -> JsString(ballot.moodlyId),
      "cookieId" -> JsString(ballot.cookieId),
      "iterationCount" -> JsNumber(ballot.iterationCount),
      "vote" -> JsNumber(ballot.vote)))
  }

}

class Ballots(tag: Tag) extends Table[Ballot](tag, "BALLOT") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def moodlyId = column[String]("MOODLY_ID", O.NotNull)

  def cookieId = column[String]("COOKIE_ID", O.NotNull)

  def iterationCount = column[Int]("ITERATION_COUNT", O.NotNull)

  def vote = column[Int]("VOTE", O.NotNull)

  def * = (id, moodlyId, cookieId, iterationCount, vote) <>((Ballot.apply _).tupled, Ballot.unapply _)

  def moodly = foreignKey("MOOLY_FK", moodlyId, Moodlies.moodlies)(_.id)
}

object Ballots {
  def ballots = TableQuery[Ballots]

  def insert(ballot: Ballot)(implicit s: Session): Long = {
    ballots.returning(ballots.map(_.id)).insert(ballot)
  }

  def findById(id: Long)(implicit s: Session): Option[Ballot] = {
    ballots.where(_.id === id).firstOption
  }
}
