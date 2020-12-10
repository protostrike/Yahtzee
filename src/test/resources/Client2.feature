Feature: Simulation of Client 2
  Scenario: Simulate several rounds with two other players
    Given Client 2 wants to connect to port 8000
    Then Client 2 is ready to play
    Then Client waits until game starts
    Then Wait for 1 second(s)
    Then Client 2 wants to score dices[2, 2, 2, 2, 2] to Category 2
    Then Wait for 1 second(s)
    And Check score 10 in category 2