package example.client.components

import example.models.SharedUserModel
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object ExampleComponent {

  case class Props(greeting: String, user: SharedUserModel)

  case class State(count: Int = 0)

  class Backend(t: BackendScope[Props, State]) {
    def plusOne() = {
      t.modState(_ => State(count = t.state.count + 1))
    }
  }

  val ExampleComponent = ReactComponentB[Props]("ExampleComponent")
    .initialState(State())
    .backend(new Backend(_))
    .render((props, state, backend) => {
    div(
      h1(props.greeting + " " + props.user.name),
      h2(state.count + " click" + (if (state.count == 1) "" else "s")),
      button(onClick --> backend.plusOne(), "+1")
    )
  })
    .build

  def apply(props: Props) = ExampleComponent(props)
}

