# JSONPlaceholder API Test Suite

![CI](https://github.com/sudaisjaved/jsonplaceholder-tests/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-11-blue)
![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-orange)

A REST API test automation suite built with **RestAssured** and **TestNG**, targeting the [JSONPlaceholder](https://jsonplaceholder.typicode.com) public API.

## Tech Stack

| Tool | Purpose |
|------|---------|
| Java 11 | Language |
| RestAssured 5.4 | HTTP client & assertions |
| TestNG 7.9 | Test framework |
| Maven | Build & dependency management |
| Hamcrest | Fluent assertion matchers |
| GitHub Actions | CI/CD pipeline |

## Test Coverage

| # | Method | Endpoint | Assertion |
|---|--------|----------|-----------|
| 1 | GET | `/posts` | Status 200, non-empty array, all required fields present |
| 2 | GET | `/posts/{id}` | Status 200, returned ID matches requested ID |
| 3 | POST | `/posts` | Status 201, response echoes all submitted fields |
| 4 | PUT | `/posts/{id}` | Status 200, all updated fields match request payload |
| 5 | DELETE | `/posts/{id}` | Status 200, empty JSON object returned |

## Project Structure

```
src/test/java/api/
├── data/
│   └── PostTestData.java      # Centralised test data (no magic values in tests)
└── tests/
    ├── BaseApiTest.java        # Shared RestAssured RequestSpecification
    └── PostsApiTest.java       # All 5 test cases
```

## Running the Tests

**Prerequisites:** Java 11+, Maven 3.6+

```bash
# Clone the repo
git clone https://github.com/sudaisjaved/jsonplaceholder-tests.git
cd jsonplaceholder-tests

# Run the full suite
mvn test
```

Test results are generated in `target/surefire-reports/`.

## Design Decisions

- **Separation of concerns** — test data lives in `PostTestData.java`, keeping test methods clean and readable
- **Shared base config** — `BaseApiTest` sets up the `RequestSpecification` once per suite via `@BeforeSuite`
- **Independent tests** — no shared mutable state between tests; each test is self-contained
- **Descriptive naming** — test methods follow the `methodUnderTest_shouldExpectedBehaviour` convention
