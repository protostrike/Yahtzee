Feature: Simulation of Client 2
  Scenario: Test connection and check update from Client 1
    Given Client 2 wants to connect to port 8000
    Then Client 2 is ready to play
    Then Client waits until game starts
    Then Wait and check update on category 1 from client 1