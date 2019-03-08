
lazy val root = (project in file("."))
  .settings(
    name := "learn-FSiS",
    organization := "com.github.shinharad",
    scalaVersion := "2.12.8",
    resolvers += Resolver.sonatypeRepo("releases"),
    scalacOptions ++= Seq(
      "-Ypartial-unification",
      "-unchecked",
      "-deprecation",
      "-feature"
//      "-Xlint"
    ),
    libraryDependencies ++= Seq(
      "com.github.mpilquist" %% "simulacrum" % "0.15.0",
      "org.scalacheck"       %% "scalacheck" % "1.14.0" % Test
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    scalafmtOnCompile in ThisBuild := true
  )



