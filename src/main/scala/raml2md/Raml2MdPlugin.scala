package raml2md

import java.io.{File, PrintWriter}

import amf.client.model.domain.WebApi
import com.overzealous.remark.{Options, Remark}
import sbt.Keys.sLog
import sbt.{Def, _}
import webapi.{Raml10, WebApiBaseUnit, WebApiDocument}

object Raml2MdPlugin extends AutoPlugin {
  override val trigger: PluginTrigger = allRequirements

  override val requires: Plugins = plugins.JvmPlugin

  object autoImport extends Raml2MdKeys

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    raml2md := raml2MdTask.value
  )

  private def raml2MdTask: Def.Initialize[Task[Unit]] = Def.task {
    val log: Logger = sLog.value

    raml2Md() match {
      case NoRamlFilesFound => log.info("No RAML files found")
      case Raml2MdSuccess => log.info("Successfully generated markdown files using RAML files")
      case Raml2MdError(errorMessage) => log.error(s"Error generating markdown files using RAML files: $errorMessage")
    }
  }

  def raml2Md(): Raml2MdResult = {
    val ramlFiles: Array[String] = findRamlFiles()

    if (ramlFiles.isEmpty) {
      NoRamlFilesFound
    } else {
      try {
        for (file <- ramlFiles) {
          val apiBaseUnit: WebApiBaseUnit = Raml10.parse(s"file://$file").get
          val apiDocument: WebApiDocument = apiBaseUnit.asInstanceOf[WebApiDocument]
          val api: WebApi = apiDocument.encodes.asInstanceOf[WebApi]

          api2Md(api)
        }
      } catch {
        case e: Exception => Raml2MdError(e.getMessage)
      }

      Raml2MdSuccess
    }
  }

  private def findRamlFiles(): Array[String] = {
    def findFiles(d: File = new File(".")): Array[File] = {
      val (dirs, files) = d.listFiles.partition(_.isDirectory)
      files ++ dirs.flatMap(findFiles)
    }

    findFiles().filter(f => f.isFile && f.getName.endsWith(".raml")).map(_.getAbsolutePath)
  }

  private def api2Md(api: WebApi): Unit = {
    val remark = new Remark(Options.pegdownAllExtensions())

    new PrintWriter(new File(s"${formatMdFilename(api.name.value())}")) {
      write(remark.convertFragment(_root_.html.api.render(api).toString()))
      close()
    }

    api.endPoints.forEach(endpoint =>
      endpoint.operations.forEach(operation =>
        new PrintWriter(new File(s"${formatMdFilename(operation.name.value())}")) {
          write(remark.convertFragment(_root_.html.endpoint_operation.render(endpoint, operation).toString()))
          close()
        }
      )
    )
  }

  private def formatMdFilename(value: String): String = {
    s"${value.replace(' ', '-').replaceAll("[^a-zA-Z0-9 -]", "").toLowerCase}.md"
  }

}