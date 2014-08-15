name := "moodly"

organizationName := "de.leanovate"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "com.h2database" % "h2" % "1.3.170",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "angularjs" % "1.2.13",
  "org.webjars" % "bootstrap" % "3.0.2",
  "org.webjars" % "underscorejs" % "1.6.0-3",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

JavaScriptBuild.javaScriptUiSettings

testOptions in Test += Tests.Argument("junitxml", "console")
