Feature: Simulation of Client 3
  Scenario: Test connection and check reset triggered by Client 1
    Given Client 3 wants to connect to port 8000
    Then Client 3 is ready to play
    Then Client waits until game starts
    Then Wait and check reset triggered by client 1