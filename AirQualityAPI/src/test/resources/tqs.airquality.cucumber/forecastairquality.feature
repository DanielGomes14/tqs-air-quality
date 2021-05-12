Feature: Get the Current and Forecast for the next 2 days of Air Quality
  Scenario: Search Forecast By City Name and CountryCode
    When I go to the tab 'localhost:3000/admin/citieslist'
    And  I want to get forecast data from  the city 'Mangualde', and country code 'PT'
    Then data should appear over 3 times, one for each day
