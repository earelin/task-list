Feature: Task lists
  As a user
  I want to manage my tasks list effectively
  So that I can group my tasks and track their status

  Scenario: Create a new task list
    Given I have an user account
    When I create a new task list with the information
        | name    | description         |
        | Errands | Tasks to do outside |
    Then I should get the task list with the information
        | name    | description         |
        | Errands | Tasks to do outside |

  Scenario: Update a task list
    Given I have an user account
      And I have a task list with the information
        | name    | description         |
        | Errands | Tasks to do outside |
    When I update the task list to
        | name    | description         |
        | Chores  | Tasks to do at home |
    Then I should get the task list with the information
        | name    | description         |
        | Chores  | Tasks to do at home |

  Scenario: Remove a task list
    Given I have an user account
      And I have a task list named "Horticulture"
    When I remove the task list "Horticulture"
    Then I should not get the task list "Horticulture"
