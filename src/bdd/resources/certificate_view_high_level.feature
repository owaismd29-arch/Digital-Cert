Feature: Certificate View High Level
  Specifications of the behavior of the Certificate View

  Background:
    Given The database contains a few certificates
    And The Certificate View is shown

  Scenario: Add a new certificate
    Given The user provides certificate data in the text fields
    When The user clicks the "Add Certificate" button
    Then The list contains the new certificate

  Scenario: Add a new certificate with an existing id
    Given The user provides certificate data in the text fields, specifying an existing id
    When The user clicks the "Add Certificate" button
    Then An error is shown containing the title of the existing certificate

  Scenario: Delete a certificate
    Given The user selects a certificate from the list
    When The user clicks the "Delete Certificate" button
    Then The certificate is removed from the list

  Scenario: Delete a not existing certificate
    Given The user selects a certificate from the list
    But The certificate is in the meantime removed from the database
    When The user clicks the "Delete Certificate" button
    Then An error is shown containing the title of the selected certificate
    And The certificate is removed from the list
