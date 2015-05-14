import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

import Settings.Project._
import Settings._

object Build extends Build {

  val appName = Settings.appName

  /**
   * We will use this setting to direct the output of the ScalaJS compiler to the resources within Dropwizard.
   */
  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

  /**
   * We will use this setting for the shared source directory
   */
  val sharedScalaDir = file(".") / Shared.toString / "src" / "main" / "scala"

  /**
   * Tasks
   */

  lazy val stage: TaskKey[Unit] = TaskKey[Unit]("stage", "Prepares the project for production.")
  lazy val stageTask = {
    println("Running `stage` task...")
    val ret = sys.process.stringSeqToProcess(Seq("/bin/bash", "-c", "sh build.sh"))
    println(ret.!!)
  }

  /**
   * We use this task to copy the source maps into the Dropwizard application
   */
  val copySourceMapsTask = Def.task {
    val scalaFiles = (Seq(sharedScalaDir, client.base) ** "*.scala").get
    for (scalaFile <- scalaFiles) {
      val target = new File((classDirectory in Compile).value, scalaFile.getPath)
      IO.copyFile(scalaFile, target)
    }
  }

  /**
   * List of projects
   */

  lazy val root =
    project.in(file(Root.toString))
      .settings(commonSettings: _*)
      .aggregate(server)

  lazy val shared =
    project.in(file(Shared.toString))
      .settings(commonSettings: _*)

  lazy val client =
    project.in(file(Client.toString))
      .dependsOn(shared)
      .enablePlugins(ScalaJSPlugin)
      .settings(clientSettings: _*)

  lazy val server =
    project.in(file(Server.toString))
      .dependsOn(shared)
      .settings(serverSettings: _*)

  /**
   * Project settings
   */
  lazy val commonSettings = Seq(
    organization := "com." + Settings.appName,
    version := Versions.app,
    name := Settings.appName,
    scalaVersion:=Versions.scala,
    stage := stageTask
  )

  lazy val serverSettings = commonSettings ++ Seq(
    name := s"$appName-server",
    scalajsOutputDir := (resourceDirectory in Compile).value / "generated",
    compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in(client, Compile)) dependsOn copySourceMapsTask,
    stage <<= stage dependsOn (fullOptJS in(client, Compile)),
    libraryDependencies ++= Dependencies.serverDeps.value
  ) ++ (
    // ask scalajs project to put its outputs in scalajsOutputDir
    Seq(fastOptJS, fullOptJS) map { packageJSKey =>
      crossTarget in(client, Compile, packageJSKey) := scalajsOutputDir.value
    }) ++ sharedDirSettings

  lazy val clientSettings = commonSettings ++ Seq(
    name := s"$appName-client",
    libraryDependencies ++= Dependencies.clientDeps.value,
    requiresDOM := true,
    jsDependencies += RuntimeDOM % "test"
  ) ++ sharedDirSettings

  lazy val sharedDirSettings = Seq(
    unmanagedSourceDirectories in Compile += new File(sharedScalaDir.getAbsolutePath)
  )

  /**
   * A simple container object for the dependencies.
   */
  object Dependencies {
    lazy val serverDeps = Def.setting(Seq(
      "io.dropwizard" % "dropwizard-core" % Versions.dropwizard,
      "io.dropwizard" % "dropwizard-assets" % Versions.dropwizard
    ))

    lazy val clientDeps = Def.setting(Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % Versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % Versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "core" % Versions.scalacss,
      "com.github.japgolly.scalacss" %%% "ext-react" % Versions.scalacss,
      "com.lihaoyi" %%% "scalarx" % Versions.scalarx,
      "com.lihaoyi" %%% "utest" % Versions.utest,
      "org.scala-js" %%% "scalajs-dom" % Versions.scalajsDom,
      "com.lihaoyi" %%% "scalatags" % Versions.scalaTags,
      "be.doeraene" %%% "scalajs-jquery" % Versions.scalajsJQuery
    ))
  }

}
