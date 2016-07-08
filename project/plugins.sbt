logLevel := Level.Warn

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

// upgrade sbt-git's jgit dependency to avoid logged IOExceptions in windows (and
// avoid an eviction warning when we do so)
libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.4.0.201606070830-r"
evictionWarningOptions in update := EvictionWarningOptions.default.withWarnTransitiveEvictions(false).withWarnDirectEvictions(false)

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.1")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.0")

// Dependencies for local versioneye plugin
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %%  "jackson-module-scala" % "2.6.1",
  "org.scalaj" %% "scalaj-http" % "1.1.5"
)
