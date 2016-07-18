import sbt._
import sbt.Keys._

object Dependencies {

  val dependencyResolvers = Seq(
    // repository for scalatestfx
    Resolver.bintrayRepo("haraldmaida", "maven")
  )

  val dependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "com.beachape" %% "enumeratum" % "1.4.5",
    "com.github.tototoshi" %% "scala-csv" % "1.3.3",
    "org.scalafx" %% "scalafx" % "8.0.92-R10",
    "org.scalactic" %% "scalactic" % "3.0.0-RC4",
    "org.typelevel" %% "cats" % "0.6.1",

    "org.scalatest" %% "scalatest" % "3.0.0-RC4" % Test,
    "org.mockito" % "mockito-core" % "2.0.79-beta"% Test,

    "io.scalatestfx" %% "scalatestfx" % "0.0.2-alpha" % UiTest.UiTest,
    // workaround for NoClassDefFoundError: scoverage/Invoker$ in UI tests
    "org.scoverage" %% "scalac-scoverage-runtime" % "1.1.1" % UiTest.UiTest,
    "com.google.code.findbugs" % "jsr305" % "3.0.1" % UiTest.UiTest
  )

  val settings = Seq(
    resolvers ++= dependencyResolvers,
    libraryDependencies ++= dependencies
  )

}
