package example.api.resources

import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

/**
 * The home page.
 */
@Path("/")
class IndexResource {

  // TODO Move this to a static file. I wasn't able to server static assets from root "/".
  @GET
  @Produces(Array(MediaType.TEXT_HTML))
  def index() = {
    """
      |<html>
      |<head>
      |  <title>Dropwizard ScalaJS React Example</title>
      |</head>
      |<body onload="App().main();">
      |  Loading...
      |  <script src="//cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react-with-addons.min.js"></script>
      |  <script src='/generated/dropwizard-scalajs-react-example-client-opt.js'></script>
      |</body>
      |</html>
    """.stripMargin
  }
}
