logLevel := Level.Warn

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.8")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

// local version of versioneye plugin used with support for custom scope mappings
// code: https://github.com/aebrett/versioneye_sbt_plugin/commit/c92851abcfae1f845ec87119a606226d4f8a9fcf
// Dependencies for local versioneye plugin
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %%  "jackson-module-scala" % "2.6.1",
  "org.scalaj" %% "scalaj-http" % "1.1.5"
)
