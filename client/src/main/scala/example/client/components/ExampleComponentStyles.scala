package example.client.components

import scalacss.Defaults._

object ExampleComponentStyles extends StyleSheet.Inline {
  import dsl._

  val title = style(
    addClassName("example-title"),
    color(red),
    fontSize(200 %%)
  )
}