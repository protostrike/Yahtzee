Feature: Roll dices tests

  Scenario: holding 0 dices in 2 re-rolls
    Given I am a Server at port 5000
    When I roll dices
    And I want to hold 0 dices and re-roll
    And I want to hold 0 dices and re-roll
    Then I reached re-roll limit
    Then clear Server

  Scenario: holding 1 dices in 2 re-rolls
    Given I am a Server at port 5001
    When I roll dices
    And I want to hold 1 dices and re-roll
    Then first 1 dices should be unchanged
    And I want to hold 1 dices and re-roll
    Then first 1 dices should be unchanged
    Then I reached re-roll limit
    Then clear Server

  Scenario: holding 2 dices in 2 re-rolls
    Given I am a Server at port 5002
    When I roll dices
    And I want to hold 2 dices and re-roll
    Then first 2 dices should be unchanged
    And I want to hold 2 dices and re-roll
    Then first 2 dices should be unchanged
    Then I reached re-roll limit
    Then clear Server

  Scenario: holding 3 dices in 2 re-rolls
    Given I am a Server at port 5003
    When I roll dices
    And I want to hold 3 dices and re-roll
    Then first 3 dices should be unchanged
    And I want to hold 3 dices and re-roll
    Then first 3 dices should be unchanged
    Then I reached re-roll limit
    Then clear Server

  Scenario: holding 4 dices in 2 re-rolls
    Given I am a Server at port 5004
    When I roll dices
    And I want to hold 4 dices and re-roll
    Then first 4 dices should be unchanged
    And I want to hold 4 dices and re-roll
    Then first 4 dices should be unchanged
    Then I reached re-roll limit
    Then clear Server

