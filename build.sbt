name := "ros-scala"


// global settings
lazy val commonSettings = Seq(
  organization := "org.ros-scala",
  version := "0.1-SNAPSHOT",
  crossPaths := true,
  scalaVersion := "2.12.3",
//  addCompilerPlugin("io.tryp" %% "splain" % "0.2.6"),

    // To sync with Maven central
  publishMavenStyle := true,

  // POM settings for Sonatype
  homepage := Some(url("https://github.com/arthur-bit-monnot/ros-scala")),
  scmInfo := Some(ScmInfo(url("https://github.com/arthur-bit-monnot/ros-scala"), "git@github.com:arthur-bit-monnot/ros-scala.git")),
  developers += Developer("abitmonn", "Arthur Bit-Monnot", "arthur.bit-monnot@laas.fr", url("https://github.com/arthur-bit-monnot")),
  licenses += ("BSD-2-Clause", url("https://opensource.org/licenses/BSD-2-Clause")),
  pomIncludeRepository := (_ => false),
  resolvers ++= Seq(
    "ROS Java" at "https://github.com/rosjava/rosjava_mvn_repo/raw/master",
    "JCenter" at "http://jcenter.bintray.com"),
  libraryDependencies += "com.lihaoyi" %% "utest" % "0.5.4" % "test",
  testFrameworks += new TestFramework("utest.runner.Framework")
)

lazy val root = project.in(file(".")).
  aggregate(msgCore, msgEncoding).
  settings(
    publish := {},
    publishLocal := {}
  )


lazy val msgCore = project
  .in(file("message-core"))
  .settings(name := "message_core")
  .settings(commonSettings: _*)
  .settings(libraryDependencies += "org.ros.rosjava_bootstrap" % "message_generation" % "0.3.0")

lazy val msgEncoding = project
  .in(file("message-encoding"))
  .dependsOn(msgCore)
  .settings(name := "message_encoding")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.ros.rosjava_core" % "rosjava" % "0.3.5",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "io.monix" %% "monix" % "2.3.0",
    "com.lihaoyi" %% "sourcecode" % "0.1.4"
  ))

lazy val msgGeneration = project
  .in(file("message-generation"))
  .settings(name := "message_generation")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
    "com.github.scopt" %% "scopt" % "3.7.0",
    "com.github.pathikrit" %% "better-files" % "3.2.0"
  ))

lazy val repl = project
  .in(file("repl"))
  .dependsOn(msgEncoding, msgGeneration)
  .settings(name := "repl")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.lihaoyi" % "ammonite" % "1.0.2" cross CrossVersion.full
  ))
