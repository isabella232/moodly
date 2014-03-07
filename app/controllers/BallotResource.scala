package controllers

import play.api.mvc._
import models.{Ballots, Moodly, Moodlies}
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json.Json

object BallotResource extends Controller {

  def create = DBAction(parse.json) {
    implicit request =>
      Ok(Json.toJson("TODO"))
  }

  def findById(id : String) = DBAction { implicit request =>
    Ballots.findById(id.toLong).map { ballot =>
      Ok(Json.toJson(ballot))
    }.getOrElse {
      NotFound(Json.toJson("Ballot not found"))
    }
  }
}
