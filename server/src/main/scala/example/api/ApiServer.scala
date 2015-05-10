package example.api

import java.util

import example.api.health.ApiHealth
import example.api.resources.{IndexResource, StatusResource}
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.jetty.ConnectorFactory
import io.dropwizard.setup.{Bootstrap, Environment}

/**
 * Runner for the API server.
 */

class ApiServer(port: Int) extends Application[ApiConfiguration] {

  private final val API_HEALTH_CHECK: String = "api_health_check"
  private final val APPLICATION_CONTEXT_PATH: String = "api"

  override def initialize(bootstrap: Bootstrap[ApiConfiguration]) {
    bootstrap.addBundle(new AssetsBundle("/generated", "/generated"))
  }

  override def run(config: ApiConfiguration, environment: Environment): Unit = {
    changePortHack(config)

    // Change the routing so we can enable static files
    environment.getApplicationContext.setContextPath(APPLICATION_CONTEXT_PATH)
    environment.jersey().setUrlPattern(s"/$APPLICATION_CONTEXT_PATH/*");

    // Setup resources
    val indexResource: IndexResource = new IndexResource
    val statusResource: StatusResource = new StatusResource

    // Endpoints
    // /
    environment.jersey().register(indexResource)
    // /status
    environment.jersey().register(statusResource)

    // Health Check
    val apiHealth: ApiHealth = new ApiHealth
    environment.healthChecks().register(API_HEALTH_CHECK, apiHealth)
  }

  /**
   * SUPER HACK
   * Changes DW's port using reflection. Used for Heroku port binding.
   * @param config The server's config
   */
  private def changePortHack(config: ApiConfiguration): Unit = {
    val serverField = config.getClass.getSuperclass.getDeclaredField("server")
    serverField.setAccessible(true)
    val server = serverField.get(config)

    val applicationConnectorsField = server.getClass.getDeclaredField("applicationConnectors")
    applicationConnectorsField.setAccessible(true)
    val ac = applicationConnectorsField.get(server).asInstanceOf[util.ArrayList[ConnectorFactory]].get(0)

    val portField = ac.getClass.getDeclaredField("port")
    portField.setAccessible(true)
    portField.set(ac, port)
  }
}

/**
 * Starts the server at `localhost:PORT`
 */
object Main {
  private final val DEFAULT_PORT: Int = 8080
  private final val CMD_SERVER: String = "server"

  def main(args: Array[String]) {
    // Expect the port to be passed in as the first arg
    val port: Int = args match {
      case Array() => DEFAULT_PORT
      case Array(num: String) => num.toInt
      case _ => throw new IllegalArgumentException
    }

    println(s"Starting server at `localhost:$port`")

    new ApiServer(port).run(CMD_SERVER)
  }
}