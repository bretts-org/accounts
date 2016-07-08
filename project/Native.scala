import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.windows.WindowsPlugin
import com.typesafe.sbt.packager.windows.WindowsPlugin.autoImport._
import sbt.Keys._

object Native {

  private val GitDescribeRegex = "([0-9\\.]+)-([0-9]+)-([a-z0-9]+)".r
  private val BaseVersionWithCommitRegex = "([0-9\\.]+)-([a-z0-9]+)".r

  lazy val plugins = Seq(JavaAppPackaging, WindowsPlugin)

  lazy val settings = Seq(
    version in Windows := {
      // strip off any non-numeric parts when building the msi package, since WiX requires
      // version to be of the form x.y.z[.b], where x < 256, y < 256 and z < 65536
      version.value match {
        case GitDescribeRegex(v, nCommits, sha) => s"$v.$nCommits"
        case BaseVersionWithCommitRegex(v, sha) => v
        case v => v
      }
    },

    // general package information
    maintainer := "Andrew Brett <git@bretts.org>",
    packageSummary := "Accounts",
    packageDescription := "Cortijo Rosario Accounts",

    // wix build information
    wixProductId := "d3b13f53-01b7-4f5c-9c36-44588a83f72c",
    wixProductUpgradeId := "a6f8f3ba-1ecc-421d-9713-4a0ecc5b528d"
  )
}
