Feature: Get the Quality of the Air in a Location that a user wants to search for.
  Scenario: Search Data by City Name and Country Code
      When I navigate to 'localhost:3000'
      And  I want to get data from  the city 'Mangualde', and country code 'PT'
      Then data should appear, like for example, the coordinates 40.60425,-7.76115
      And some indicators like 'co', 'so2', or 'no2'
  Scenario: Search Data by Invalid City Name and Country Code
      When I navigate to 'localhost:3000'
      And  I want to get data from  the city 'CidadeInvalida', and country code 'FFFF'
      Then Error should appear with the message 'City And/Or Country Not Found!'
