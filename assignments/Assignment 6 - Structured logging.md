# Assignment 6: Structured Logging

## User story

As an operator/support engineer, I want application logs emitted as structured JSON with a
correlation id per request, so that I can search, filter, and correlate logs across a request's
lifecycle in a log aggregation system.

## Acceptance criteria

- Application logs are emitted as JSON (one JSON object per line) including at minimum:
  timestamp, log level, logger name, message, and a `correlationId`/`requestId` field.
- Every incoming HTTP request is assigned a correlation id (reused from an incoming
  `X-Correlation-Id` header if present, otherwise generated), and that id is present on every
  log line emitted while handling that request, and returned to the caller in the response
  headers.
- Logs never contain passwords, tokens, Authorization headers, or full request/response bodies
  that could contain PII (e.g., a user's email may be acceptable to log at debug level per team
  convention — decide and document the policy explicitly; do not log request bodies wholesale
  by default).
- Existing log statements continue to work (no broken logging calls) and existing tests are
  unaffected by the log format change.

## Constraints

- Do not log the full request or response body indiscriminately at INFO level — that risks
  leaking sensitive data. If body logging is desired for debugging, gate it behind a DEBUG level
  and explicitly scrub sensitive fields.
- Do not hard-code environment-specific logging destinations (e.g., a specific log-shipping
  endpoint) in code — use externalized configuration.
- Correlation id propagation must work across the single request thread (using MDC or
  equivalent) without leaking between concurrent requests.

## Expected implementation slice

- Add a structured JSON log encoder (e.g., Logback with `logstash-logback-encoder`, or
  equivalent) configured in `logback-spring.xml` / `application.yml`.
- Add a servlet filter (`OncePerRequestFilter`) that reads/generates a correlation id, puts it
  in MDC (and clears it after the request), and sets it on the response header.
- Update `GlobalExceptionHandler` and key service methods to log meaningfully (e.g., a
  duplicate-email attempt at WARN, unexpected errors at ERROR) without including sensitive
  payloads.
- Document the logging policy (what is safe to log, what is not) in the README or a short
  `docs/logging.md`.

## Testing expectations

- Test verifying the correlation id filter: generates an id when absent, reuses the header value
  when present, and it appears in the response header.
- Test (or manual verification documented in the PR/notes) that log output is valid JSON and
  includes the correlation id field.
- Test verifying that a request with a simulated sensitive field (e.g., a password-like field on
  a future endpoint) does not appear in the captured log output — a regression guard for the
  "never log sensitive data" rule.

## Time-boxed implementation guidance

Target: **75–105 minutes**. Prioritize: (1) correlation id filter + MDC propagation, (2)
structured JSON output. Treat log-level tuning across every existing log statement as a stretch
goal — focus depth on the request lifecycle and error paths first.

## Trade-offs and follow-up work

- Deferred: distributed tracing (span ids across service calls), log sampling/rate limiting,
  shipping logs to a specific external aggregator.
- Consider adding the correlation id to audit entries (if Assignment 4 is also implemented) to
  link audit history back to the originating request.

## Quality requirements

- Logging configuration lives in configuration files, not scattered `System.out.println` calls.
- Correlation id is available via MDC to any logger without manual plumbing through method
  signatures.
- No sensitive data appears in logs — verified by a test, not just code review.

## Key business rules out of controllers

- Correlation id generation/propagation strategy and logging policy (what fields are safe) are
  implemented in a filter/config layer, not scattered as ad hoc logic inside `UserController`.

## Key persistent details out of API response

- Internal logging configuration (appender destinations, encoder settings) and MDC keys used
  internally are operational concerns and must not be exposed in any API response payload.
