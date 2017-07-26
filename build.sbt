
lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.apache.commons" % "commons-compress" % "1.10")
)

lazy val hello = taskKey[Unit]("An example task")

lazy val root = (project in file("."))
  .settings(
    commonSettings
  )
