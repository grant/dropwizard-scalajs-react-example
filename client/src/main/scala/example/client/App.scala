package example.client

import example.client.components.ExampleComponentStyles
import japgolly.scalajs.react.React
import japgolly.scalajs.react.extra.router.BaseUrl
import org.scalajs.dom
import scalacss.Defaults._
import scalacss.ScalaCssReact._


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport("App")
object App extends JSApp {
  @JSExport
  def main(): Unit = {
    // build a baseUrl, this method works for both local and server addresses (assuming you use #)
    val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))
    val router = MainRouter.router(baseUrl)

    // Load the css styles
    ExampleComponentStyles.addToDocument()

    // tell React to render the router in the document body
    React.render(router(), dom.document.body)
  }
}
