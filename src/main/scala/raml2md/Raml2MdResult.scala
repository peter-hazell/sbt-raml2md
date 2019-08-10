package raml2md

sealed trait Raml2MdResult

case object NoRamlFilesFound extends Raml2MdResult

case object Raml2MdSuccess extends Raml2MdResult

case class Raml2MdFailure(message: String) extends Raml2MdResult