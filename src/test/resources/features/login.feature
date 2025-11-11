Feature: API Automation for Login and Authenticated Request

  Scenario: Validate login and access protected API using token
    Given User logs in to the application with valid credentials
    When User sends a GET request to protected API using the token
    Then The response status code should be 200
    And The response should contain expected JSON fields
