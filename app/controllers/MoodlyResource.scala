package controllers

import play.api.mvc._
import models.{Moodly, Moodlies}
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json.Json

object MoodlyResource extends Controller {

  def create = DBAction(parse.json) {
    implicit request =>
      (request.body \ "intervalDays").asOpt[Int].map { intervalDays =>
        val moodly = new models.Moodly(intervalDays)
        Moodlies.insert(moodly)
        Ok(Json.toJson(moodly))
      }.getOrElse {
        BadRequest("missing intervalDays")
      }
  }

  def findById(id : String) = DBAction { implicit request =>
    Moodlies.findById(id).map { moodly:Moodly =>
      Ok(Json.toJson(moodly))
    }.getOrElse {
      NotFound(Json.toJson("Moodly not found"))
    }
  }

  def currentIterationCounter(id : String) = DBAction { implicit request =>
      Moodlies.findById(id).map { moodly:Moodly =>
        Ok(Json.obj("currentIterationCount" -> moodly.currentIterationCount()))
      }.getOrElse {
        NotFound(Json.toJson("Moodly not found"))
      }
    }

}
