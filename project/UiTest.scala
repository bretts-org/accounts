import java.io.File

import sbt._
import sbt.Keys._

object UiTest {
  lazy val UiTest = config("test-ui") extend Test

  private def defaultExtDirs = sys.props("java.ext.dirs")

  lazy val settings = inConfig(UiTest)(Defaults.testSettings) ++ Seq(
    fork in UiTest := true,
    javaOptions in UiTest += "-Djava.ext.dirs=" + (baseDirectory.value / "lib" / "ext") + File.pathSeparator + defaultExtDirs,
    javaOptions in UiTest += "-Dtestfx.robot=glass",
    javaOptions in UiTest += "-Dglass.platform=Monocle",
    javaOptions in UiTest += "-Dmonocle.platform=Headless",
    javaOptions in UiTest += "-Dprism.order=sw"
  )
}
