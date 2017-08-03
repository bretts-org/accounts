import sbt._
import sbt.Keys._

object Common {

  lazy val settings = Seq(
    name := "Accounts",
    organization := "org.bretts",

    scalaVersion := "2.12.3",

    scalacOptions := Seq(
      "-target:jvm-1.8",
      "-Xfuture", "-Xexperimental",
      "-Xcheckinit",
      "-Xfatal-warnings",
      "-deprecation", "-unchecked", "-feature",
      "-Xlint:_",
      "-Ywarn-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-infer-any",
      // Ignore parameter warnings below, since they give unhelpful warnings
      "-Ywarn-nullary-override", "-Ywarn-nullary-unit", "-Ywarn-unused:-params,_", "-Ywarn-unused-import"
      // "-Ywarn-numeric-widen", "-Ywarn-value-discard" <-- overly stringent warnings
    ),

    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-o", "-u", (target.value / "junit-reports").toString)
  )
}
