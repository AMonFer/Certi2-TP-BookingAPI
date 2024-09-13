Feature: Booking endpoint
  Background: Booking endpoints should allow to get, create, bookings
   #get booking by valid id
  Scenario: /employee/{id} should return an specific employee
    Given I perform a GET call to the booking endpoint with id "5"
    Then I verify that the status code is 200
   #get booking by invalid id
  Scenario: /employee/{id} returns 404 when an invalid id is sent
    Given I perform a GET call to the booking endpoint with id "letras"
    Then I verify that the status code is 404
    #get booking by invalid format id
  Scenario: /employee/{id} returns 404 when an invalid format id is sent
    Given I perform a GET call to the booking endpoint with id "cinco"
    Then I verify that the status code is 404
    #post booking with valid data
  Scenario Outline: Create booking with valid data and returns 200
    Given I perform a POST call to the booking endpoint with the following data
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then I verify that the status code is 200
    And I verify that the body does not have size 0
    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Adrian    | Montaño  | 100        | false       | 2016-10-16 | 2019-07-06 | Breakfast       |
      | Jorge     | Perez    | 200        | true        | 2017-08-01  | 2020-05-20  | Lunch            |
  #post booking with nothing
  Scenario: Create booking with nothing and returns 400
    Given I perform A POST call with nothing
    Then I verify that the status code is 400
  #post booking with bad sintaxis json
  Scenario Outline: Create booking with a bad sintaxis json
    Given I perform a POST call to the booking endpoint with the following data but a bad json
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then I verify that the status code is 400
    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Adrian    | Montaño  | 100        | false       | 2016-10-16 | 2019-07-06 | Breakfast       |