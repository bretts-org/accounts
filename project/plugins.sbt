logLevel := Level.Warn

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

// upgrade sbt-git's jgit dependency to avoid logged IOExceptions in windows (and
// avoid an eviction warning when we do so)
libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.5.0.201609210915-r"
evictionWarningOptions in update :=
  EvictionWarningOptions.default.withWarnTransitiveEvictions(false).withWarnDirectEvictions(false)

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.7")

// local version of versioneye plugin used with support for custom scope mappings
// code: https://github.com/aebrett/versioneye_sbt_plugin/commit/c92851abcfae1f845ec87119a606226d4f8a9fcf
// Dependencies for local versioneye plugin
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %%  "jackson-module-scala" % "2.6.1",
  "org.scalaj" %% "scalaj-http" % "1.1.5"
)
