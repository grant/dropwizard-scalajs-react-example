package example.client

import example.client.modules.{Dashboard, MainMenu}
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

// define a trait to access all application routes
trait AppLinks {}

object MainRouter extends RoutingRules {
  // register the modules and store locations
  val homeLocation = register(rootLocation(Dashboard.component))

  def appLinks(router: Router): AppLinks = new AppLinks {}

  // redirect all invalid routes to dashboard
  override protected val notFound = redirect(homeLocation, Redirect.Replace)

  /**
   * Creates the basic page structure under the body tag.
   *
   * @param ic
   * @return
   */
  override protected def interceptRender(ic: InterceptionR) = {
    <.div(
      MainMenu(MainMenu.Props(ic.loc, ic.router)),
      // currently active module is shown in this container
      <.div(^.className := "page")(ic.element)
    )
  }
}
