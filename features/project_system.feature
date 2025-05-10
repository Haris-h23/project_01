Feature: Project Management System Core and system holder

  Scenario: Creating projects through the system
    Given I use the shared system
    When I create a project named "Apollo" through the system
    Then the system should contain a project named "Apollo"

  Scenario: Getting all projects
    Given I use the shared system
    When I create a project named "Hermes" through the system
    And I create a project named "Athena" through the system
    Then the system should return 3 projects

  Scenario: Getting or creating employees
    Given I use the shared system
    When I get or create an employee with initials "AB"
    And I get or create an employee with initials "CD"
    Then the system should return 2 employees
    And the system should contain an employee with initials "AB"
