package utils

object FilenameFormatter {

  def formatMdFilename(value: String): String =
    s"${value.replace(' ', '-').replaceAll("[^a-zA-Z0-9 -]", "").toLowerCase}.md"

}
