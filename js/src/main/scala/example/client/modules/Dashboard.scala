package example.client.modules

import example.client.MainRouter
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.all._

object Dashboard {

  def testClick() = {
    println("hi")
  }

  // create the React component for Dashboard
  val component = ReactComponentB[MainRouter.Router]("Dashboard")
    .render(router => {
    // get internal links
    val appLinks = MainRouter.appLinks(router)
    div(
      h2("Dashboard"),
      h3("hello there nope"),
      h6("yup"),
      input(),
      span(className := "hi", onClick --> testClick(), "hello"),
      if (Math.random() > 0.5) {
        span("hi")
      } else {
        EmptyTag
      }
    )
  }).build
}
