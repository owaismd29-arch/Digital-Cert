Feature: Certificate View
  Specifications of the behavior of the Certificate View

  Scenario: The initial state of the view
    Given The database contains the certificates with the following values
      | id | title    | issuedTo | issuedBy | year |
      |  1 | Java SE  | Owais    | Oracle   | 2024 |
      |  2 | AWS Cloud| Bilal    | Amazon   | 2023 |
    When The Certificate View is shown
    Then The list contains elements with the following values
      | 1 | Java SE   | Owais | Oracle | 2024 |
      | 2 | AWS Cloud | Bilal | Amazon | 2023 |

  Scenario: Add a new certificate
    Given The Certificate View is shown
    When The user enters the following values in the text fields
      | id | title   | issuedTo | issuedBy | year |
      |  1 | Java SE | Owais    | Oracle   | 2024 |
    And The user clicks the "Add Certificate" button
    Then The list contains the new certificate

  Scenario: Add a new certificate with an existing id
    Given The database contains the certificates with the following values
      | id | title   | issuedTo | issuedBy | year |
      |  1 | Java SE | Owais    | Oracle   | 2024 |
    And The Certificate View is shown
    When The user enters the following values in the text fields
      | id | title         | issuedTo | issuedBy | year |
      |  1 | Duplicate Title | Owais  | Some Org | 2025 |
    And The user clicks the "Add Certificate" button
    Then An error is shown containing the following values
      | 1 | Java SE | Owais | Oracle | 2024 |
