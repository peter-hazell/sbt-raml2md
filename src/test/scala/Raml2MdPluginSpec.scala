import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import raml2md.{Raml2MdPlugin, Raml2MdSuccess}

import scala.io.Source

class Raml2MdPluginSpec extends FlatSpec with Matchers {

  private def clearMdFiles(): Array[Boolean] = {
    findMdFiles().map(_.delete())
  }

  private def findMdFiles(): Array[File] = {
    def findFiles(d: File = new File(".")): Array[File] = {
      val (dirs, files) = d.listFiles.partition(_.isDirectory)
      files ++ dirs.flatMap(findFiles)
    }

    findFiles().filter(f => f.isFile && f.getName.endsWith(".md") && f.getName != "README.md")
  }

  "raml2Md" should
    "produce markdown files for any RAML file that is found in the project" in {
    clearMdFiles()

    val result = Raml2MdPlugin.raml2Md()

    val outputFiles: Array[File] = findMdFiles()
    val outputFilenames: Array[String] = outputFiles.map(_.getName)
    val outputFileContents: Array[String] = outputFiles.map(file => Source.fromFile(file).getLines().mkString("\n"))

    result shouldBe Raml2MdSuccess
    outputFiles.length shouldBe 2
    outputFilenames.contains("test-api.md") shouldBe true
    outputFilenames.contains("get-test-value.md") shouldBe true

    clearMdFiles()
  }

}
