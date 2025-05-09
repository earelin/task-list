Feature: Task
  As a user
  I want to manage my tasks list effectively
  So that I can organize my time

  Scenario: Create a new task
    Given I have an user account
      And I have a task list named "Errands"
    When I create a new task with the information
      | name        | description        |
      | Supermarket | Buy food for lunch |
    Then I should get the task with the information
      | name        | description        |
      | Supermarket | Buy food for lunch |

  Scenario: Update a task
    Given I have an user account
      And I have a task list named "Errands"
      And I have a task with the information
        | name        | description        |
        | Supermarket | Buy food for lunch |
    When I update the task to
      | name    | description         |
      | Restaurant  | Order food for lunch |
    Then I should get the task with the information
      | name    | description         |
      | Restaurant  | Order food for lunch |

  Scenario: Remove a task
    Given I have an user account
      And I have a task list named "Horticulture"
      And I have a task with the information
        | name        | description        |
        | Supermarket | Buy food for lunch |
    When I remove the task "Supermarket"
    Then I should not get the task "Supermarket"

  Scenario: Add a tag to a task
    Given I have an user account
      And I have a task list named "Errands"
      And I have a task with the information
        | name        | description        |
        | Supermarket | Buy food for lunch |
    When I add the tag "Shopping" to the task "Supermarket"
    Then I should get the task with the tag "Shopping" set
