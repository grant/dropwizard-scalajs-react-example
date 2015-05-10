package example.client.pages

import example.client.MainRouter
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.all._

object NotFoundPage {

  val component = ReactComponentB[MainRouter.Router]("NotFound")
    .render(router => {
    div(
      h1("Route Not Found"),
      if (Math.random() > 0.5) {
        span(" (Random) ")
      } else {
        EmptyTag
      }
    )
  }).build
}
