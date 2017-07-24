
val derby = "org.apache.derby" % "derby" % "10.4.1.3"


lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.1"
)

val someSetting =  Seq(
  name := "hello",
  libraryDependencies += derby
)

lazy val hello = taskKey[Unit]("An example task")

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    someSetting,
    autoScalaLibrary := true
  )