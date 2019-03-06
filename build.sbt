
lazy val root = (project in file("."))
  .settings(
    name := "learn-FSiS",
    organization := "com.github.shinharad",
    scalaVersion := Dependencies.scalaV,
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= Dependencies.coreDependencies,
    scalacOptions ++= Seq(
      //        "-Xfatal-warnings",
      "-Ypartial-unification",
      // For Scala 2.13+ with Simulacrum
//      "-Ymacro-annotations",
      "-unchecked",
      "-deprecation",
      "-feature"
//      "-Xlint"
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9"),
    // For Simulacrum
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    scalafmtOnCompile in ThisBuild := true
  )



