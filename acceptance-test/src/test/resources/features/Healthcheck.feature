Feature: Microservice Healthcheck report
  As a developer or devops engineer
  I want to check the health of the microservice
  So that I can ensure the service is running properly

  Scenario: Check health of the microservice
    Given I have access to the microservice
    When I send a healthcheck request
    Then I should receive a valid response
    And the response should report the service as healthy
