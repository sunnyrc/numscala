name := "numscala"

version := "0.1"

scalaVersion := "2.11.3"

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases"  at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies  ++= Seq(
  "org.scalatest"    %% "scalatest"      % "2.1.6"  % "test",
  "org.scalanlp"     %% "breeze"         % "0.10",
  "org.scalanlp"     %% "breeze-natives" % "0.10",
  "org.sameersingh.scalaplot" % "scalaplot" % "0.0.4"
)

