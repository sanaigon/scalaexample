
lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0-SNAPSHOT",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

lazy val hello = taskKey[Unit]("An example task")

lazy val root = (project in file("."))
  .settings(
    commonSettings
  )
