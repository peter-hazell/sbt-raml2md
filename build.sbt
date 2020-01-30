name := "sbt-raml2md"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.raml" % "webapi-parser" % "0.2.0",
  "com.kotcrab.remark" % "remark" % "1.0.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

resolvers += "MuleSoftReleases" at "https://repository.mulesoft.org/nexus/content/repositories/public/"

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl)
  .settings(
    name := "sbt-raml2md",
    organization := "com.petehazell",
    version := "0.3.0",
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    sbtPlugin := true,
    bintrayVcsUrl := Some("git@github.com:peter-hazell/sbt-raml2md.git")
  )
