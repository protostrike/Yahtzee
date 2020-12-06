Feature: Roll dices tests

  Scenario: holding 0 dices in 2 re-rolls
    Given I am a player
    When I roll dices
    And I want to hold 0 dices and re-roll
    Then I check after re-roll

  Scenario: holding 1 dices in 2 re-rolls
    Given I am a player
    When I roll dices
    And I want to hold 1 dices and re-roll
    Then first 1 dices should be unchanged
    Then I check after re-roll

  Scenario: holding 2 dices in 2 re-rolls
    Given I am a player
    When I roll dices
    And I want to hold 2 dices and re-roll
    Then first 2 dices should be unchanged
    Then I check after re-roll

  Scenario: holding 3 dices in 2 re-rolls
    Given I am a player
    When I roll dices
    And I want to hold 3 dices and re-roll
    Then first 3 dices should be unchanged
    Then I check after re-roll

  Scenario: holding 4 dices in 2 re-rolls
    Given I am a player
    When I roll dices
    And I want to hold 4 dices and re-roll
    Then first 4 dices should be unchanged
    Then I check after re-roll