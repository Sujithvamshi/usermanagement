# Evaluation Criteria

This rubric applies to every assignment in [../assignments/](../assignments/). Each assignment
also lists assignment-specific **Acceptance criteria** and **Quality requirements** — score
those first, then use this document for the cross-cutting dimensions below.

## Scoring dimensions (100 points total)

| Dimension | Points | What "good" looks like |
|---|---|---|
| Functional correctness | 30 | Meets the assignment's acceptance criteria; existing endpoints/tests still pass |
| Architecture & separation of concerns | 20 | Business rules stay in the application/service layer; controllers stay thin; JPA/persistence details never leak into API responses |
| Testing | 20 | New behavior is covered by unit and/or integration tests; edge cases (empty results, invalid input, unauthorized access) are exercised |
| Code quality & readability | 10 | Clear naming, no dead code, no unnecessary abstraction, consistent with existing conventions |
| Security & data handling | 10 | No hard-coded secrets; no logging of passwords/tokens/PII; authZ checks applied where relevant |
| Backward compatibility | 5 | Existing API contracts and response envelopes are unchanged unless the assignment explicitly requires a change |
| Time-box discipline & trade-off communication | 5 | Delivered a coherent slice within the time box and clearly articulated what was deferred and why |

## Pass / fail gates (must all be true regardless of score)

- The project still builds and the existing test suite still passes.
- No secrets (API keys, passwords, tokens) are committed or hard-coded.
- No sensitive data (passwords, tokens, full audit payloads containing PII) is written to logs.
- JPA entities are not returned directly from any REST endpoint.
- New business logic is not placed inside `web` controllers.

## Rating scale

- **Exceeds expectations (90-100):** All acceptance criteria met, thoughtful tests including
  edge cases, clean diffs, clear articulation of trade-offs.
- **Meets expectations (70-89):** Core acceptance criteria met, adequate tests, minor gaps in
  polish or edge-case handling.
- **Below expectations (50-69):** Partial functionality, weak or missing tests, some
  architecture violations (e.g., logic leaking into controllers).
- **Does not meet bar (<50):** Fails a pass/fail gate, or acceptance criteria largely unmet.

## Assignment-specific evaluation notes

- **Assignment 1 (search/sort/filter/empty):** Verify filtering/sorting is done server-side (or
  clearly justified client-side) and that an explicit empty state is distinguishable from a
  loading or error state.
- **Assignment 2 (CSV preview):** Verify the preview step never commits data until explicitly
  confirmed, and that malformed rows are reported without failing the entire import.
- **Assignment 3 (auth/RBAC soft delete-restore):** Verify unauthorized roles cannot soft-delete
  or restore, and that soft-deleted users are excluded from default list/search results.
- **Assignment 4 (audit history):** Verify audit records are immutable, never exposed with raw
  persistence identifiers unnecessarily, and capture who/what/when for each change.
- **Assignment 5 (health check):** Verify the endpoint reflects real dependency status (e.g.,
  database reachability) and doesn't leak internal configuration details.
- **Assignment 6 (structured logging):** Verify logs are structured (JSON), include a
  correlation/request id, and never contain passwords, tokens, or full request bodies with PII.
