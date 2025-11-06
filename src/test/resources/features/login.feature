Feature: Petstore API Authentication

  Scenario: login and get user details
    Given Login to petstore with username "string" and password "string"
    Then Valid token should be received
    When Request petstore user details for "string"
    Then User should see the username in response as "string"
