Feature: Simulation of Client 3
  Scenario: Simulate several rounds with two other players
    Given Client 3 wants to connect to port 8000
    Then Client 3 is ready to play
    Then Client waits until game starts
    Then Wait for 2 second(s)
    And Check reset triggered by client 2