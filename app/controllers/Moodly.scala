package controllers

import play.api.mvc._

object Moodly extends Controller {

  def newMoodly = Action(parse.json) { request =>
    val uuid = java.util.UUID.randomUUID.toString
    Ok(s"{'uuid': '$uuid'}")
  }
}
