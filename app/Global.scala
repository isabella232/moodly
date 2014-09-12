import models.{Ballots, Ballot, Moodlies, Moodly}
import org.joda.time.DateTime
import play.api._
import play.api.mvc._
import play.filters.gzip.GzipFilter

import scala.util.Random

object Global extends WithFilters(new GzipFilter()) with GlobalSettings {

  override def onStart(app: Application): Unit = {

    if (app.mode == Mode.Dev) {
      Logger.debug("dev mode - populate the database to ease testing")

      val twentyDaysAgo = DateTime.now().minusDays(20)
      val moodly = new Moodly(intervalDays = 1, start = twentyDaysAgo)
      val moodlyId = moodly.id

      play.api.db.slick.DB(app).withTransaction { implicit session =>
        Moodlies.insert(moodly)
        Logger.debug(s"inserted moodly $moodly")
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
