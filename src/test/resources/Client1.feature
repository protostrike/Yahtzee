Feature: Simulation of Client 1
  Background:
    Given A server starts at port 8000

  Scenario: Simulate several rounds with two other players
    Given Client 1 wants to connect to port 8000
    Then Client 1 is ready to play
    Then Client waits until game starts
    And Client 1 wants to score dices[1, 1, 1, 1, 1] to Category 1
    And Wait for 1 second(s)
    Then Check score 5 in category 1
    And Wait for 2 second(s)
    Then Check update on category 2 from client 2
