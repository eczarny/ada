import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "com.divisiblebyzero"
  val buildVersion = "1.0"
  val buildScalaVersion = "2.9.1"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion
  )
}

object Dependencies {
  val log4j = "log4j" % "log4j" % "1.2.16"
}

object AdaBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val ada = Project(
    "ada",
    file("."),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(log4j),
      mainClass in (Compile, run) := Some("com.divisiblebyzero.ada.Main"),
      fork in run := true
    )
  )
}
