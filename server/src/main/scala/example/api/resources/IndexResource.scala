package example.api.resources

import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

/**
 * The home page.
 */
@Path("/")
class IndexResource {
  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  def get(): String = {
    "index\n"
  }
}
