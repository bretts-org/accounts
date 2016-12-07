import java.io.File

import sbt._
import sbt.Keys._

object UiTest {
  lazy val UiTest = config("test-ui") extend Test

  private def defaultExtDirs = sys.props("java.ext.dirs")

  lazy val settings = inConfig(UiTest)(Defaults.testSettings) ++ Seq(
    fork in UiTest := true,
    // lib/ext contains a custom build of Monocle for java 1.8.0_92+
    // code: https://github.com/aebrett/Monocle/commit/64afcc153dde0cb2d8811578457b312b550f13bf
    javaOptions in UiTest += "-Djava.ext.dirs=" + (baseDirectory.value / "lib" / "ext") + File.pathSeparator + defaultExtDirs,
    javaOptions in UiTest += "-Dtestfx.robot=glass",
    javaOptions in UiTest += "-Dglass.platform=Monocle",
    javaOptions in UiTest += "-Dmonocle.platform=Headless",
    javaOptions in UiTest += "-Dprism.order=sw",
    // Workaround for JVM EXCEPTION_ACCESS_VIOLATION crashes in javafx_font.dll when headless
    javaOptions in UiTest += "-Dprism.text=t2k"
  )
}
