Feature: Get Basic Cache Statistics
  Scenario: No requests were made
    When I go over the page 'localhost:3000/admin/cache'
    Then some statistics should appear like 'Number of Requests', 'Number of Misses','Number of Hits'
    And all of them should have the values 0
  Scenario: Two requests were made
    When I go over the page 'localhost:3000'
    And Type in 'Mangualde','PT'
    Then some statistics should appear like 'Number of Requests', 'Number of Misses','Number of Hits'
    And number of requests and misses 1, and number of hits 0