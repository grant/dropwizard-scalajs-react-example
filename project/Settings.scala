object Settings {
  val appName = "dropwizard-scalajs-react-example"

  object Project extends Enumeration {
    val Root = Value(".")
    val Shared = Value("shared")
    val Client = Value("client")
    val Server = Value("server")
  }

  object Versions {
    val app = "1.0-SNAPSHOT"
    val scala = "2.11.5"

    val dropwizard = "0.8.1"
    val bootstrap = "3.3.2"
    val newRelic = "3.12.1"
    val react = "0.12.1"
    val scalarx = "0.2.7"
    val scalajsDom = "0.8.0"
    val scalajsJQuery = "0.8.0"
    val scalajsReact = "0.8.1"
    val scalaTags = "0.4.5"
    val scalaTest = "2.2.0"
    val scalaTestPlus = "1.2.0"
    val utest = "0.3.0"
    val upickle = "0.2.6"
  }

}