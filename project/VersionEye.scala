import sbt._
import com.versioneye.VersionEyePlugin
import com.versioneye.VersionEyePlugin.autoImport._

object VersionEye {

  lazy val plugins = Seq(VersionEyePlugin)

  lazy val settings =
    sys.env.get("VERSIONEYE_API_KEY").map { t =>
      apiKey in versioneye := t
    }.toSeq ++ Seq(
      existingProjectId in versioneye := "577f754a5bb1390040177b76",
      scopeMappings ++= Map(UiTest.UiTest.name -> UiTest.UiTest.name)
    )
}
