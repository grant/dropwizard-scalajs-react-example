package example.client.pages

import example.client.MainRouter
import example.client.components.ExampleComponent
import example.models.SharedUserModel
import japgolly.scalajs.react.ReactComponentB

object ExamplePage {
  val component = ReactComponentB[MainRouter.Router]("ExamplePage")
    .render(router => {
    val testUser = new SharedUserModel(name = "Grant Timmerman", username = "grant")
    ExampleComponent(ExampleComponent.Props(greeting = "Hello", user = testUser))
  }).build
}
