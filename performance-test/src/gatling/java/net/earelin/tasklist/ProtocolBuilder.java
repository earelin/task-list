package net.earelin.tasklist;

import static io.gatling.javaapi.http.HttpDsl.http;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import io.gatling.javaapi.http.HttpProtocolBuilder;

public final class ProtocolBuilder {
  public static final HttpProtocolBuilder httpProtocol = http
      .baseUrl(getBaseUrl())
      .acceptHeader("application/json")
      .contentTypeHeader("application/json");

  private static String getBaseUrl() {
    var baseUrlEnv = System.getenv("BASE_URL");
    return isNotBlank(baseUrlEnv) ? baseUrlEnv : "http://localhost:8080";
  }

  private ProtocolBuilder() {
    // Private constructor to prevent instantiation
  }
}
