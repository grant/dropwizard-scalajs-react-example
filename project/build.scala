import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

object Build extends Build {

  val appName = "dropwizard-scalajs-react-example"

  /**
   * We will use this setting to direct the output of the ScalaJS compiler to the resources within the Play module.
   */
  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

  /**
   * We will use this setting for the shared source directory
   */
  val sharedScalaDir = file(".") / "shared" / "main" / "scala"

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
    project.in(file("."))
      .settings(commonSettings: _*)
      .aggregate(server)

  lazy val shared =
    project.in(file("shared"))
      .settings(commonSettings: _*)

  lazy val client =
    project.in(file("client"))
      .dependsOn(shared)
      .enablePlugins(ScalaJSPlugin)
      .settings(clientSettings: _*)

  lazy val server =
    project.in(file("server"))
      .dependsOn(shared)
      .settings(serverSettings: _*)

  /**
   * Project settings
   */
  lazy val commonSettings = Seq(
    organization := "com.dropwizard-scalajs-react-example",
    version := "1.0-SNAPSHOT",
    name := "dropwizard-scalajs-react-example",
    scalaVersion := "2.11.4",
    stage := stageTask
  )

  lazy val serverSettings = commonSettings ++ Seq(
    name := s"$appName-server",
    //    scalajsOutputDir := (classDirectory in Compile).value / "public" / "javascripts",
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
    unmanagedSourceDirectories in Compile += new File((baseDirectory.value / ".." / "shared" / "src/main/scala").getCanonicalPath)
  )

  /**
   * A simple container object for the dependencies.
   */
  object Dependencies {
    lazy val serverDeps = Def.setting(Seq(
      "io.dropwizard" % "dropwizard-core" % "0.8.1",
      "io.dropwizard" % "dropwizard-assets" % "0.8.1"
    ))

    lazy val clientDeps = Def.setting(Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % "0.8.0",
      "com.github.japgolly.scalajs-react" %%% "extra" % "0.8.0",
      "com.lihaoyi" %%% "scalarx" % "0.2.7",
      "com.lihaoyi" %%% "utest" % "0.3.0",
      "org.scala-js" %%% "scalajs-dom" % Versions.scalajsDom,
      "com.lihaoyi" %%% "scalatags" % Versions.scalaTags,
      "be.doeraene" %%% "scalajs-jquery" % Versions.scalajsJQuery
    ))
  }

}
