# Assignment 5: Health Check

## User story

As an operator running this service, I want a health check endpoint that reflects whether the
application and its dependencies (e.g., the database) are actually healthy, so that monitoring/
orchestration tooling can make accurate up/down decisions.

## Acceptance criteria

- A `GET /api/health` (or Spring Boot Actuator `/actuator/health`, clearly documented either
  way) endpoint returns HTTP 200 with a status of `UP` when the application and database are
  reachable, and a non-200 status (e.g., 503) with status `DOWN` when a critical dependency
  (the database) is unreachable.
- The response includes at minimum: overall status, and a per-component breakdown (e.g.,
  `database`) with its own status — but **never** internal configuration details (connection
  strings, credentials, internal hostnames).
- The endpoint responds quickly (no long-running queries) and does not require authentication
  (it's meant for infrastructure probes), but must not leak sensitive information regardless of
  caller identity.
- Existing endpoints and tests remain unaffected.

## Constraints

- Do not hard-code any credentials or connection details in the health check code — reuse the
  existing `DataSource`/JPA configuration to verify connectivity.
- Do not expose stack traces or exception messages from a failed dependency check in the
  response body; log them (without sensitive data) instead.
- Keep the health-check logic testable independent of the full Spring context where reasonable.

## Expected implementation slice

- If using Spring Boot Actuator: add the `spring-boot-starter-actuator` dependency, enable the
  `health` endpoint, and add a custom `HealthIndicator` (or use the built-in DataSource health
  indicator) with sanitized output. Document the actuator endpoint's base path.
- If building a custom endpoint: add a `HealthCheckUseCase`/service that pings the database
  (e.g., a lightweight `SELECT 1` or repository `count()`), and a controller endpoint mapping
  the result to a sanitized `HealthResponse` DTO.
- Ensure the response status code reflects health (200 for UP, 503 for DOWN), not always 200.

## Testing expectations

- Unit/integration test verifying `UP` status and 200 when the database is reachable (default
  H2 test setup).
- Test verifying the endpoint does not include sensitive configuration values in its response
  (e.g., assert the response body does not contain the JDBC URL or credentials).
- If a custom health check is implemented, a unit test simulating a database failure (e.g., a
  mocked port/repository throwing an exception) verifying it degrades to `DOWN`/503 without
  throwing an unhandled exception.

## Time-boxed implementation guidance

Target: **45–75 minutes**. This is intentionally the smallest assignment. Prioritize a correct,
sanitized `UP`/`DOWN` response over building an elaborate multi-component health model.

## Trade-offs and follow-up work

- Deferred: liveness vs. readiness distinction, per-dependency timeouts/circuit breakers,
  historical health metrics/alerting integration.
- Consider adding a `/actuator/info` (build version, git commit) alongside health if useful for
  operations, kept equally sanitized.

## Quality requirements

- Health check does not perform expensive operations (no full table scans).
- Sanitized output verified by an explicit test, not just manual inspection.
- Endpoint is documented in the README (path, expected responses, sample payloads).

## Key business rules out of controllers

- The logic that determines "healthy" (e.g., what dependency checks matter, thresholds) lives
  in a service/use case, not inline in the controller method body.

## Key persistent details out of API response

- Datasource configuration (JDBC URL, driver class, pool settings, credentials) must never
  appear in the health response, even when a dependency is down.
