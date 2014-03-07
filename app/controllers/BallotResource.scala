package controllers

import play.api.mvc._
import models.{Ballot, Ballots, Moodly, Moodlies}
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json.Json

object BallotResource extends Controller {

  def create(moodlyId: String) = DBAction(parse.json) {
    implicit request =>
      Json.fromJson[Ballot](request.body).asOpt.map { ballot =>
        val ballotId = Ballots.insert(Ballot(0, moodlyId, ballot.cookieId, ballot.iterationCount, ballot.vote))
        Ok(Json.toJson(Ballot(ballotId, moodlyId, ballot.cookieId, ballot.iterationCount, ballot.vote)))
      }.getOrElse {
        BadRequest(Json.toJson("Failed to parse json"))
      }
  }

  def findById(moodlyId: String, id: String) = DBAction {
    implicit request =>
      Ballots.findById(id.toLong).map {
        ballot =>
          Ok(Json.toJson(ballot))
      }.getOrElse {
        NotFound(Json.toJson("Ballot not found"))
      }
  }
}
