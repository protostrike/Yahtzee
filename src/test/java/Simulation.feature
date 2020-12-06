Feature: Game Simulation Test

  Scenario: Playing with one player
    Given start a launcher with 1 player(s)
    Then player(s) play the game
    Then game ends

  Scenario: Playing with two players
    Given start a launcher with 2 player(s)
    Then player(s) play the game
    Then game ends