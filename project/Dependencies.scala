import sbt._

object Dependencies {
  val scalaV = "2.12.8"
  
  lazy val simulacrumDependencies = Seq(
    "com.github.mpilquist" %% "simulacrum" % "0.15.0"
  )

  lazy val coreDependencies =
    simulacrumDependencies
    
}
