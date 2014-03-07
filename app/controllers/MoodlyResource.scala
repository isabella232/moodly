package controllers

import play.api.mvc._
import play.libs.Json

object MoodlyResource extends Controller {

  def create = Action(parse.json) { request =>
    val uuid = java.util.UUID.randomUUID.toString
    val ts = System.currentTimeMillis / 1000
    val intervall = (request.body \ "intervall").as[Int]
    Ok(s"{'uuid': '$uuid'}")
  }
}
