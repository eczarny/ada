import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "com.divisiblebyzero"
  val buildVersion = "1.0"
  val buildScalaVersion = "2.9.0-1"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt := ShellPrompt.buildShellPrompt)
}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info(s: => String) {}
    def error(s: => String) {}
    def buffer[T](f: => T): T = f
  }

  val current = """\*\s+([\w-]+)""".r

  def gitBranches = ("git branch --no-color" lines_! devnull mkString)

  val buildShellPrompt = {
    (state: State) =>
      {
        val currentBranch = current findFirstMatchIn gitBranches map (_ group (1)) getOrElse "-"
        val currentProject = Project.extract(state).currentProject.id

        "%s:%s:%s> ".format(currentProject, currentBranch, BuildSettings.buildVersion)
      }
  }
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
