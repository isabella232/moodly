package models

import java.sql.Timestamp
import java.util.Date
import org.joda.time
import org.joda.time.DateTime

import org.specs2.mutable.Specification

class MoodlyTest extends Specification {

  "the current iteration count" should {
    "ignore the time of day" in {
      val moodly = new Moodly(intervalDays = 1)
      // today at current time
      moodly.currentIterationCount() shouldEqual 0

      // today at 1 am
      moodly.currentIterationCount(new DateTime().withTime(1,0,0,0).toDate) shouldEqual 0

      // today at 23 pm
      moodly.currentIterationCount(new DateTime().withTime(23,0,0,0).toDate) shouldEqual 0

      // tomorrow at 1 am
      moodly.currentIterationCount(new DateTime().plusDays(1).withTime(1,0,0,0).toDate) shouldEqual 1

    }
  }
}
