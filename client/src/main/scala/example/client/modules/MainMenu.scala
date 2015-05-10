package example.client.modules

import example.client.MainRouter
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.prefix_<^._

object MainMenu {

  case class Props(activeLocation: MainRouter.Loc, router: MainRouter.Router)

  case class MenuItem(label: (Props) => ReactNode, location: MainRouter.Loc)

  class Backend(t: BackendScope[Props, _]) extends OnUnmount {
    def mounted(): Unit = {}
  }

  private val menuItems = Seq(
    MenuItem(_ => "Home", MainRouter.homeLocation)
  )

  private val MainMenu = ReactComponentB[Props]("MainMenu")
    .stateless
    .backend(new Backend(_))
    .render((P, _, B) => {
    <.ul(^.className := "nav")(
      // build a list of menu items
      for (item <- menuItems) yield {
        <.li((P.activeLocation == item.location) ?= (^.className := "active"),
          P.router.link(item.location)(item.label(P))
        )
      }
    )
  })
    .componentDidMount(_.backend.mounted())
    .build

  def apply(props: Props) = MainMenu(props)
}
