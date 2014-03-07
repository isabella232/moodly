package controllers

import play.api.mvc._
import models.{Moodly, Moodlies}
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json.Json

object MoodlyResource extends Controller {

  def create = DBAction(parse.json) {
    implicit request =>
      val moodly = new models.Moodly((request.body \ "intervalDays").as[Int])
      Moodlies.insert(moodly)
      Ok(Json.toJson(moodly))
  }

  def findById(id : String) = DBAction { implicit request =>
    Moodlies.findById(id).map { moodly:Moodly =>
      Ok(Json.toJson(moodly))
    }.getOrElse {
      NotFound(Json.toJson("Moodly not found"))
    }
  }
}
