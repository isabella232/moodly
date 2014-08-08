import models.{Ballots, Ballot, Moodlies, Moodly}
import play.api._

import scala.util.Random

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {

    if (app.mode == Mode.Dev) {
      Logger.debug("dev mode - populate the database to ease testing")

      val moodly = new Moodly(2)
      val moodlyId = moodly.id

      play.api.db.slick.DB(app).withTransaction { implicit session =>
        Moodlies.insert(moodly)
        for (i <- 0 to 20) {
          val cookieId = s"test-participant-$i"
          val iteration = Random.nextInt(20)
          val vote = Random.nextInt(5) + 1
          val ballot = Ballot(0, moodlyId, cookieId, iteration, vote)
          Ballots.insert(ballot)
          Logger.debug(s"inserted ballot $ballot")
        }
      }


      Logger.info(
        s"""check populated moodly on following URLS:
             |- http://localhost:9000/#/voting/$moodlyId
             |- http://localhost:9000/#/stats/$moodlyId
           """.stripMargin)
    }
  }
}
