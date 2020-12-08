Feature: Game Simulation test

  Scenario: one player simulation
    Given I start a Server at port 6080
    And player 1 named "Alex"
    When player one gets ready
    And players play this game
    Then game ends
    Then do cleanup

  Scenario: three players simulation
    Given I start a Server at port 6081
    And player 1 named "A"
    When player one needs more players
    And player 2 named "B"
    When player one needs more players
    And player 3 named "C"
    When player one gets ready
    And players play this game
    Then game ends
    Then do cleanup