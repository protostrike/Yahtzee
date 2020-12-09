Feature: Scoring tests

  Scenario: scoring 0 in Ones
    Given I am a ScoreCard
    When I score [2, 2, 3, 4, 5] in category 1
    Then total score should be 0

  Scenario: scoring non-0 in Ones
    Given I am a ScoreCard
    When I score [1, 2, 3, 1, 5] in category 1
    Then total score should be 2

  Scenario: scoring 0 in Twos
    Given I am a ScoreCard
    When I score [1, 1, 3, 4, 5] in category 2
    Then total score should be 0

  Scenario: scoring non-0 in Twos
    Given I am a ScoreCard
    When I score [1, 2, 4, 2, 5] in category 2
    Then total score should be 4

  Scenario: scoring 0 in Threes
    Given I am a ScoreCard
    When I score [2, 1, 1, 4, 5] in category 3
    Then total score should be 0

  Scenario: scoring non-0 in Threes
    Given I am a ScoreCard
    When I score [3, 2, 3, 1, 5] in category 3
    Then total score should be 6

  Scenario: scoring 0 in Fours
    Given I am a ScoreCard
    When I score [2, 2, 3, 1, 5] in category 4
    Then total score should be 0

  Scenario: scoring non-0 in Fours
    Given I am a ScoreCard
    When I score [1, 2, 3, 4, 5] in category 4
    Then total score should be 4

  Scenario: scoring 0 in Fives
    Given I am a ScoreCard
    When I score [2, 2, 3, 4, 1] in category 5
    Then total score should be 0

  Scenario: scoring non-0 in Fives
    Given I am a ScoreCard
    When I score [1, 2, 3, 1, 5] in category 5
    Then total score should be 5

  Scenario: scoring 0 in Sixes
    Given I am a ScoreCard
    When I score [2, 2, 3, 4, 5] in category 6
    Then total score should be 0

  Scenario: scoring non-0 in Sixes
    Given I am a ScoreCard
    When I score [1, 2, 6, 6, 5] in category 6
    Then total score should be 12

  Scenario: scoring 0 in Three of A Kind
    Given I am a ScoreCard
    When I score [2, 5, 3, 2, 1] in category 7
    Then total score should be 0

  Scenario: scoring non-0 in Three of A Kind
    Given I am a ScoreCard
    When I score [2, 2, 3, 3, 2] in category 7
    Then total score should be 12

  Scenario: scoring 0 in Four of A Kind
    Given I am a ScoreCard
    When I score [2, 5, 3, 2, 1] in category 8
    Then total score should be 0

  Scenario: scoring non-0 in Four of A Kind
    Given I am a ScoreCard
    When I score [2, 2, 2, 3, 2] in category 8
    Then total score should be 11

  Scenario: scoring 0 in Small Straight
    Given I am a ScoreCard
    When I score [2, 2, 2, 2, 2] in category 9
    Then total score should be 0

  Scenario: scoring non-0 in Small Straight
    Given I am a ScoreCard
    When I score [1, 2, 3, 6, 4] in category 9
    Then total score should be 30

  Scenario: scoring 0 in Large Straight
    Given I am a ScoreCard
    When I score [2, 2, 2, 2, 2] in category 10
    Then total score should be 0

  Scenario: scoring non-0 in Large Straight
    Given I am a ScoreCard
    When I score [5, 2, 3, 6, 4] in category 10
    Then total score should be 40

  Scenario: scoring 0 in Full House
    Given I am a ScoreCard
    When I score [2, 2, 2, 2, 2] in category 11
    Then total score should be 0

  Scenario: scoring non-0 in Full House
    Given I am a ScoreCard
    When I score [2, 2, 3, 3, 2] in category 11
    Then total score should be 25

  Scenario: scoring 0 in Yahtzee
    Given I am a ScoreCard
    When I score [2, 5, 3, 2, 1] in category 12
    Then total score should be 0

  Scenario: scoring non-0 in Yahtzee
    Given I am a ScoreCard
    When I score [2, 2, 2, 2, 2] in category 12
    Then total score should be 50

  Scenario: scoring in Chance
    Given I am a ScoreCard
    When I score [2, 2, 3, 3, 2] in category 13
    Then total score should be 12

  Scenario: getting upper bonus
    Given I am a ScoreCard
    When I score [1, 1, 1, 3, 4] in category 1
    And I score [2, 2, 2, 3, 4] in category 2
    And I score [3, 3, 1, 3, 4] in category 3
    And I score [4, 4, 1, 3, 4] in category 4
    And I score [5, 5, 5, 3, 4] in category 5
    And I score [6, 6, 6, 3, 4] in category 6
    Then total score should be 98

  Scenario: getting Yahtzee bonus
    Given I am a ScoreCard
    When I score [1, 1, 1, 1, 1] in category 12
    And I score [1, 1, 1, 1, 1] in category 1
    Then total score should be 155

  Scenario: scoring in the same category twice
    Given I am a ScoreCard
    When I score [1, 1, 1, 1, 1] in category 1
    And I score [2, 1, 2, 1, 2] in category 1
    Then total score should be 5