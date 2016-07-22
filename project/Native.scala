import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.jdkpackager.JDKPackagerPlugin
import com.typesafe.sbt.packager.jdkpackager.JDKPackagerPlugin.autoImport._
import com.typesafe.sbt.packager.windows.WindowsPlugin
import com.typesafe.sbt.packager.windows.WindowsPlugin.autoImport._
import sbt._
import sbt.Keys._

object Native {

  private val GitDescribeRegex = """([0-9\.]+)-([0-9]+)-([a-z0-9]+)(?:-SNAPSHOT)?""".r
  private val BaseVersionWithCommitRegex = """([0-9\.]+)-([a-z0-9]+)(?:-SNAPSHOT)?""".r
  private val GitTagRegex = """([0-9\.]+)(?:-SNAPSHOT)?""".r

  private def os = sys.props("os.name").split(" ").head.toLowerCase

  lazy val plugins = os match {
    case "windows" => Seq(JavaAppPackaging, WindowsPlugin)
    case "mac" => Seq(JDKPackagerPlugin)
    case _ => Seq()
  }

  lazy val settings = os match {
    case "windows" => Seq(
      version in Windows := {
        // strip off any non-numeric parts when building the msi package, since WiX requires
        // version to be of the form x.x.y[.y], where 0 <= x < 256 and 0 <= y < 65536
        version.value match {
          case GitDescribeRegex(v, numAdditionalCommits, sha) => s"$v.$numAdditionalCommits"
          case BaseVersionWithCommitRegex(v, sha) => v
          case GitTagRegex(v) => v
          case v => v
        }
      },

      // general package information
      maintainer := "Andrew Brett <git@bretts.org>",
      packageSummary := "Accounts",
      packageDescription := "Cortijo Rosario Accounts",

      // wix build information
      wixProductId := "d3b13f53-01b7-4f5c-9c36-44588a83f72c",
      wixProductUpgradeId := "a6f8f3ba-1ecc-421d-9713-4a0ecc5b528d",

      lightOptions ++= Seq(
        "-out",
        (target.value / "windows" / s"${name.value}-${version.value}.msi").toString
      )
    )

    case "mac" => Seq(
      jdkPackagerBasename := s"${name.value}-${version.value}",
      jdkPackagerType := "dmg",
      jdkAppIcon := Some((resourceDirectory in Compile).value / "icon" / "coins-euro.icns")
    )

    case _ => Seq()
  }
}
