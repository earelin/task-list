package net.earelin.tasklist.application.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthcheckTest {
   @LocalServerPort
   private int port;

   @BeforeEach
   void setUp() {
     RestAssured.port = port;
   }

   @Test
   void healthcheck() {
     given()
         .when()
           .get("/actuator/health")
         .then()
           .statusCode(200);
   }
}
