# REST API Test Framework Java

![Java](https://img.shields.io/badge/Java-21-blue)
![RestAssured](https://img.shields.io/badge/RestAssured-6.0.0-green)
![TestNG](https://img.shields.io/badge/TestNG-7.12.0-orange)
![Allure](https://img.shields.io/badge/Allure-2.34.0-purple)
![CI](https://github.com/zakharchenkoo/rest-api-test-framework-java/actions/workflows/api-tests.yml/badge.svg)

A portfolio-level REST API automation framework built with **Java 21, RestAssured, TestNG, Allure, JavaFaker, JSON Schema Validator, Retrofit and GitHub Actions**.

The target API is the public **GitHub REST API**:

```text
https://api.github.com
```

The framework uses HTTPS only and does **not** disable SSL certificate validation. Authentication is optional and can be provided via the `GITHUB_TOKEN` environment variable or GitHub Actions secrets.

---

## Why this project is stronger than a simple test collection

This is not just a folder with API tests. It demonstrates a layered test framework design:

```text
configuration layer
        ↓
request builder layer
        ↓
API client layer
        ↓
test data layer
        ↓
test layer
        ↓
reporting layer
```

The framework includes:

- reusable API client layer
- request builder pattern
- typed Java models for API responses
- JSON Schema validation
- TestNG DataProvider with CSV test data
- JavaFaker-based negative test data generation
- Allure steps and request/response attachments
- sensitive data masking for Authorization headers
- Retrofit typed client as an engineering comparison
- GitHub Actions CI pipeline

---

## Repository structure

```text
rest-api-test-framework-java/
 ├── .github/workflows/api-tests.yml
 ├── src/test/java/com/alona/qa/
 │   ├── base/BaseTest.java
 │   ├── client/GitHubClient.java
 │   ├── client/request/ApiRequest.java
 │   ├── config/ConfigManager.java
 │   ├── data/TestDataFactory.java
 │   ├── dataproviders/RepositoryDataProvider.java
 │   ├── models/
 │   ├── retrofit/
 │   ├── tests/
 │   └── utils/
 ├── src/test/resources/
 │   ├── config.properties
 │   ├── allure.properties
 │   ├── schemas/
 │   └── testdata/repositories.csv
 ├── pom.xml
 ├── testng.xml
 └── README.md
```

---

## Test coverage

### Users API

- `GET /users/{username}` positive scenario
- public user response schema validation
- unknown user negative scenario with `404 Not Found`
- `GET /users/{username}/repos` pagination check

### Repositories API

- `GET /repos/{owner}/{repo}` data-driven tests from CSV
- repository response schema validation
- repository owner and identity checks

### Search API

- `GET /search/repositories`
- query parameters: `q`, `sort`, `order`, `per_page`
- response schema validation
- search result content checks

### Rate limit API

- `GET /rate_limit`
- response schema validation
- core rate limit checks

### Retrofit comparison

The framework also includes a Retrofit-based client to compare two approaches:

```text
RestAssured → testing-oriented API client with fluent assertions and schema validation
Retrofit    → typed engineering-style API client based on Java interfaces
```

---

## Configuration

Main config file:

```text
src/test/resources/config.properties
```

Example:

```properties
base.url=https://api.github.com
api.version=2022-11-28
github.token.env=GITHUB_TOKEN
request.timeout.seconds=20
```

You can override properties from the command line:

```bash
mvn clean test -Dbase.url=https://api.github.com
```

---

## Authentication

The tests can run without a token because they use public GitHub API endpoints.

For a higher rate limit, set a token as an environment variable:

### PowerShell

```powershell
$env:GITHUB_TOKEN="your_token_here"
mvn clean test
```

### Bash

```bash
export GITHUB_TOKEN="your_token_here"
mvn clean test
```

Do not hardcode tokens in Java code or properties files.

---

## Run tests locally

```bash
mvn clean test
```

Run a specific test class:

```bash
mvn clean test -Dtest=GitHubUserApiTest
```

---

## Allure report

Generate and open Allure report:

```bash
mvn allure:serve
```

Generate static report:

```bash
mvn allure:report
```

Allure results are created under:

```text
target/allure-results
```

---

## CI/CD

GitHub Actions workflow:

```text
.github/workflows/api-tests.yml
```

The pipeline:

1. checks out the repository
2. installs Java 21
3. runs `mvn -B clean test`
4. uploads Allure results
5. uploads Surefire reports

The workflow passes `GITHUB_TOKEN` through environment variables, not through hardcoded configuration.

---

## Production-like practices demonstrated

- HTTPS is used by default.
- SSL certificate validation is not disabled.
- Secrets are not stored in the repository.
- Authorization headers are masked in Allure attachments.
- Test data is separated from test logic.
- API clients are separated from test classes.
- JSON response contracts are validated with schemas.
- CI runs tests automatically on push and pull requests.

---

## Future improvements

- add GitHub Pages publication for Allure reports
- add Maven profiles for smoke/regression suites
- add retry only for infrastructure-level failures
- add WireMock-based tests for unstable external dependencies
- add contract tests
- add test summary report after execution
