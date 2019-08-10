import java.io.{File, PrintWriter}

import org.scalatest.{Matchers, WordSpec}
import raml2md.{Raml2MdFailure, Raml2MdPlugin, Raml2MdSuccess}

class Raml2MdPluginSpec extends WordSpec with Matchers {

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

  "raml2Md" should {
    "return a Raml2MdSuccess result and generate markdown files for any RAML file that is found in the project" in {
      clearMdFiles()

      val result = Raml2MdPlugin.raml2Md()

      val outputFiles: Array[File] = findMdFiles()
      val outputFilenames: Array[String] = outputFiles.map(_.getName)

      result shouldBe Raml2MdSuccess
      outputFiles.length shouldBe 2
      outputFilenames.contains("test-api.md") shouldBe true
      outputFilenames.contains("get-test-value.md") shouldBe true

      clearMdFiles()
    }

    "return a Raml2MdFailure result if there is a failure generating markdown files for any RAML file that is found in the project" in {
      val invalidRamlFile = new File("invalid.raml")

      new PrintWriter(invalidRamlFile) {
        write("Invalid raml contents")
        close()
      }

      val result = Raml2MdPlugin.raml2Md()

      result shouldBe Raml2MdFailure("amf.core.exception.UnsupportedVendorException: Cannot parse document with specified vendor: RAML 1.0")

      invalidRamlFile.delete()
    }
  }

}
