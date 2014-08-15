name := "moodly"

organizationName := "de.leanovate"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  filters,
  "com.h2database" % "h2" % "1.3.170",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  // WebJars (i.e. client-side) dependencies
  "org.webjars" % "bootstrap" % "3.1.1-2" exclude("org.webjars", "jquery"),
  "org.webjars" % "requirejs" % "2.1.14-1",
  "org.webjars" % "underscorejs" % "1.6.0-3",
  "org.webjars" % "angularjs" % "1.2.18",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Apply RequireJS optimization, digest calculation and gzip compression to assets
pipelineStages := Seq(rjs, digest, gzip)

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

testOptions in Test += Tests.Argument("junitxml", "console")
