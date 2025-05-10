Feature: Activity management

  Scenario: Create a new activity
    Given I create an activity named "Design" with 50 hours, start week 12, and end week 16 in year 20225
    Then the activity name should be "Design"
    And the budgeted hours should be 50
    And the start week should be 12
    And the end week should be 16
    And the activity status should be "NONE"

  Scenario: Edit activity properties
    Given I create an activity named "Temp" with 10 hours, start week 1, and end week 2 in year 2025
    When I change the name to "Final"
    And I set the budgeted hours to 100
    And I set the start week to 5
    And I set the end week to 10
    Then the activity name should be "Final"
    And the budgeted hours should be 100
    And the start week should be 5
    And the end week should be 10

  Scenario: Update status
    Given I create an activity named "QA" with 30 hours, start week 20, and end week 22 in year 2025
    When I set the activity status to "APPROVED"
    Then the activity status should be "APPROVED"

  Scenario: assigning a user to an activity
    Given I create an activity named "Development" with 40 hours, start week 15, and end week 20 in year 2025
    And I have a user named "JL" 
    When I assign the user to the activity
    Then the activity should have the user assigned to it
