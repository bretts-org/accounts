import com.versioneye.VersionEyePlugin
import com.versioneye.VersionEyePlugin.autoImport._

object VersionEye {

  lazy val plugins = Seq(VersionEyePlugin)

  lazy val settings =
    sys.env.get("VERSIONEYE_API_KEY").map { t =>
      apiKey in versioneye := t
    }.toSeq ++ Seq(
      existingProjectId in versioneye := "577f5aad5bb13900493de5bf"
    )
}
