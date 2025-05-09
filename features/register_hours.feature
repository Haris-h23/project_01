Feature: Registering hours on an activity

  Scenario: An employee registers hours
    Given I create an activity named "Implementation" with 100 hours, start week 10, and end week 15
    And I have a user named "MH"
    When I assign the user to the activity
    And the user registers 8 hours
    Then the user should have 8 registered hours
    And the total registered hours should be 8

  Scenario: Multiple employees register hours
    Given I create an activity named "Testing" with 50 hours, start week 12, and end week 16
    And I have a user named "AB"
    And I assign the user to the activity
    And the user registers 4 hours
    And I have a user named "CD"
    And I assign the user to the activity
    And the user registers 6 hours
    Then the total registered hours should be 10
