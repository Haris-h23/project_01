Feature: Project management

  Scenario: Create a project and verify its details
    Given I create a project named "Apollo"
    Then the project name should be "Apollo"
    And the project number should be a number greater than 25000
    And the project should not be marked as done

  Scenario: Set and check project leader
    Given I create a project named "Venus"
    When I assign "jt" as the project leader
    Then the project leader should be "jt"
    And "JT" should be recognized as the project leader

  Scenario: Mark project as done
    Given I create a project named "Saturn"
    When I mark the project as done
    Then the project should be marked as done

  Scenario: Add activities to a project
    Given I create a project named "Mars"
    And I create an activity named "Testing" with 20 hours, start week 5, and end week 7
    When I add the activity to the project
    Then the project should contain 1 activity
