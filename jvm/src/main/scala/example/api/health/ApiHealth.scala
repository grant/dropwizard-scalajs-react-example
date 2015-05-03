package example.api.health

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result

/**
 * Checks the health of the API.
 */
class ApiHealth extends HealthCheck {
  override def check(): Result = {
    return Result.healthy()
  }
}
