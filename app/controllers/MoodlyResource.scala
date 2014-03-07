package controllers

import play.api.mvc._
import play.libs.Json
import models.Moodlies
import play.api.db.slick._
import play.api.Play.current

object MoodlyResource extends Controller {

  def create = DBAction(parse.json) { implicit request =>
    val moodly = new models.Moodly((request.body \ "intervalDays").as[Int])
    Moodlies.insert(moodly)
    Ok(s"{'uuid': '${moodly.id}'}")
  }
}
