Feature: Simulation of Client 1
  Background:
    Given A server starts at port 8000

  Scenario: Test connection and score once
    Given Client 1 wants to connect to port 8000
    Then Client 1 is ready to play
    Then Client waits until game starts
    And Client 1 wants to score dices[1, 1, 1, 1, 1] to Category 1
    Then Wait and check score 5 in category 1