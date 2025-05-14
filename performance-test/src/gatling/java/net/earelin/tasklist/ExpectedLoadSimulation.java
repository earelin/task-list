package net.earelin.tasklist;

import static io.gatling.javaapi.core.CoreDsl.global;
import static net.earelin.tasklist.ScenariosBuilder.taskListUserJourney;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;

import io.gatling.javaapi.core.Simulation;
import java.time.Duration;

public final class ExpectedLoadSimulation extends Simulation {
  {
    setUp(taskListUserJourney.injectOpen(rampUsersPerSec(1).to(40).during(Duration.ofMinutes(1))))
        .normalPausesWithStdDevDuration(Duration.ofSeconds(2))
        .protocols(ProtocolBuilder.httpProtocol)
        .assertions(
            global().successfulRequests().percent().gt(99.0),
            global().responseTime().mean().lt(500)
        )
        .maxDuration(Duration.ofMinutes(5));
  }
}
