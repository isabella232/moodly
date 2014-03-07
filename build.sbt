name := "moodly"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "com.h2database" % "h2" % "1.3.170",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  "org.webjars" % "angularjs" % "1.2.13",
  "org.webjars" % "bootstrap" % "3.0.2"
)     

play.Project.playScalaSettings
