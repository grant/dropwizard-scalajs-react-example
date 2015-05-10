package example.client

import example.client.pages.{NotFoundPage, ExamplePage}
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.all._

object MainRouter extends RoutingRules {
  // Register the routes
  val root = register(rootLocation(ExamplePage.component))
  override val notFound = render(NotFoundPage.component)

  /**
   * Creates the basic page structure under the body tag.
   */
  override protected def interceptRender(ic: InterceptionR) = {
    div(className := "page")(ic.element)
  }
}
