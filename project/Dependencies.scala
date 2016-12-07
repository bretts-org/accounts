import sbt._
import sbt.Keys._

object Dependencies {

  val dependencyResolvers = Seq(
    // repository for scalatestfx
    Resolver.bintrayRepo("haraldmaida", "maven")
  )

  val dependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "com.beachape" %% "enumeratum" % "1.5.1",
    "com.github.tototoshi" %% "scala-csv" % "1.3.4",
    "org.scalafx" %% "scalafx" % "8.0.102-R11",
    // local version of controlsfx used with TableFilter enhancements
    // code: https://bitbucket.org/aebrett/controlsfx/commits/00df155857d75265b919f12e4df468b24206c00b?at=default
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.typelevel" %% "cats" % "0.8.1",

    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "org.mockito" % "mockito-core" % "2.2.21"% Test,

    // local veresion of scalatestfx used with update to scala 2.12.1
    // code: https://github.com/aebrett/ScalaTestFX/commit/b8ad930f985f0d9a059211481181062b444bda91
    // needed for scalatestfx
    "org.testfx" % "testfx-core" % "4.0.4-alpha",

    // workaround for NoClassDefFoundError: scoverage/Invoker$ in UI tests
    "org.scoverage" %% "scalac-scoverage-runtime" % "1.3.0" % UiTest.UiTest,
    "com.google.code.findbugs" % "jsr305" % "3.0.1" % UiTest.UiTest
  )

  val settings = Seq(
    resolvers ++= dependencyResolvers,
    libraryDependencies ++= dependencies
  )

}
