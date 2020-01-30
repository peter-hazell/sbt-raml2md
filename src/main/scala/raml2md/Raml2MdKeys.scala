package raml2md

import sbt._

trait Raml2MdKeys {
  lazy val raml2md = taskKey[Unit]("Generates markdown from any RAML files found within a project")
}
