# Assignment 4: Audit History

## User story

As an administrator, I want to see a history of changes made to a user record (what changed,
who changed it, and when), so that I can investigate issues and demonstrate accountability.

## Acceptance criteria

- Every create/update/soft-delete/restore operation on a user produces an immutable audit
  record capturing: subject user id, action type (`CREATED`, `UPDATED`, `DELETED`, `RESTORED`),
  a diff or snapshot of changed fields, the acting principal (or `SYSTEM` if unauthenticated
  actions are allowed), and a timestamp.
- `GET /api/users/{id}/history` returns the audit trail for a user, ordered newest-first, via a
  dedicated `AuditEntryResponse` DTO — never the raw audit persistence entity.
- Audit records are never updated or deleted through the API (append-only).
- If a user has no history yet (e.g., just created, no further changes), the endpoint returns an
  empty list, not an error.
- Audit entries never contain sensitive data (e.g., if an authentication assignment is also
  implemented, tokens/passwords must never appear in a diff or snapshot).

## Constraints

- Do not couple audit writing to the controller — it must happen as part of the use case/service
  logic so it can't be bypassed by calling the service directly.
- Do not block the primary operation if audit writing fails silently swallow the error either —
  decide and document a clear strategy (e.g., same transaction as the primary write) and apply
  it consistently.
- Avoid an overly generic "event bus" abstraction for this exercise — a direct call from each use
  case to an `AuditRecorder`/`AuditService` port is sufficient.

## Expected implementation slice

- Add an `AuditEntry` persistence entity (`adapters/persistence`) and a corresponding domain
  concept only if the application layer needs to reason about it (otherwise keep it adapter-
  local behind a port).
- Add an outbound port (e.g., `AuditRepositoryPort`) and have `CreateUserService`,
  `UpdateUserService`, `DeleteUserService` (and `RestoreUserUseCase` if Assignment 3 is also
  implemented) write an audit entry as part of the same transaction as the primary change.
- Add `GetUserHistoryUseCase` and a controller endpoint `GET /api/users/{id}/history`.
- Frontend: a simple history panel/table on the user detail view (or accessible from the user
  list row actions).

## Testing expectations

- Unit tests verifying each mutating use case (create/update/delete/restore) writes exactly one
  audit entry with correct action type and changed-field data.
- Test verifying updates that don't actually change any field either don't create a no-op audit
  entry, or clearly document why they do (pick one behavior and test it).
- Integration test verifying `GET /api/users/{id}/history` returns entries newest-first and an
  empty list for a user with no recorded changes.
- Test verifying audit entries never include sensitive fields.

## Time-boxed implementation guidance

Target: **120–150 minutes**. Prioritize: (1) audit writes on create/update/delete, (2) the
history read endpoint. Treat "diff" computation (vs. simple full-snapshot capture) as a stretch
goal — a full snapshot per change is an acceptable simplification within the time box.

## Trade-offs and follow-up work

- Deferred: pagination of history for very active records, retention/archival policy, field-
  level diffing UI.
- Consider whether audit writes should be resilient to primary-transaction rollback (i.e.,
  should an audit entry ever be written if the underlying change fails? Recommended: no — keep
  them in the same transaction).

## Quality requirements

- Audit writing logic is centralized (one port/service), not duplicated per use case.
- Audit entries are append-only at the persistence layer (no update/delete repository methods
  exposed for `AuditEntry`).
- No sensitive data captured in audit snapshots/diffs.

## Key business rules out of controllers

- What constitutes a change worth auditing, how snapshots/diffs are computed, and the audit
  action taxonomy live in the application/service layer, not in `UserController`.

## Key persistent details out of API response

- The `AuditEntry` JPA entity (ids, foreign keys, storage-specific fields) is never returned
  directly; `GET /api/users/{id}/history` returns a mapped `AuditEntryResponse` with only the
  fields relevant to a consumer.
