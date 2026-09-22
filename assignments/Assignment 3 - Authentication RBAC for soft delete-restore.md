# Assignment 3: Authentication & RBAC for Soft Delete / Restore

## User story

As a system operator, I want only authorized roles (e.g., `ADMIN`) to be able to soft-delete or
restore a user, so that regular users cannot remove or resurrect registration records, while
soft-deleted users are hidden from normal views but recoverable.

## Acceptance criteria

- Users have a `deletedAt` (nullable) attribute at the persistence layer. `DELETE
  /api/users/{id}` becomes a **soft delete** (sets `deletedAt`), not a hard delete, and requires
  an authenticated caller with the `ADMIN` role — otherwise returns HTTP 403.
- A new `POST /api/users/{id}/restore` endpoint clears `deletedAt`, also requiring `ADMIN`.
  Restoring a user that is not soft-deleted is a no-op that returns a clear message, not an
  error.
- `GET /api/users` and `GET /api/users/{id}` exclude soft-deleted users by default. Soft-deleted
  users behave as if they were hard-deleted from the perspective of any endpoint that doesn't
  explicitly opt in to seeing them.
- Unauthenticated requests to any endpoint return HTTP 401; authenticated requests without the
  required role return HTTP 403 — both via the existing `ErrorResponse` envelope.
- Authentication mechanism (e.g., a simple bearer token / in-memory user store for this
  exercise) must not hard-code credentials in source; use configuration/environment variables
  with safe local defaults for development only.

## Constraints

- Do not remove the existing hard-delete test expectations without replacing them — update
  existing tests to reflect soft-delete semantics rather than leaving them broken.
- Do not log tokens, passwords, or Authorization headers.
- Keep authorization checks declarative where possible (e.g., method security annotations)
  rather than scattered `if` checks in controllers.

## Expected implementation slice

- Add `deletedAt` to `UserJpaEntity` (persistence-only; not on the domain `User` unless needed
  for restore logic) and a Liquibase/DDL-auto update.
- Add a minimal authentication mechanism (e.g., Spring Security with an in-memory
  `UserDetailsService` or a simple API-key/bearer-token filter) and role-based method security
  (`@PreAuthorize("hasRole('ADMIN')")`) on delete/restore use cases.
- Update `UserRepositoryPort`/adapter queries to filter out soft-deleted rows by default.
- Update `DeleteUserUseCase` to perform a soft delete; add `RestoreUserUseCase`.
- Frontend: gate delete/restore actions in the UI based on the current user's role; show a
  clear message when a non-admin attempts a restricted action (or hide the action entirely).

## Testing expectations

- Unit tests: soft delete sets `deletedAt`; restore clears it; restoring a non-deleted user is a
  no-op; list/get exclude soft-deleted users by default.
- Security tests: unauthenticated request to protected endpoints returns 401; authenticated
  non-admin returns 403; admin succeeds.
- Integration test covering the full lifecycle: create → soft delete (as admin) → verify hidden
  from list/get → restore (as admin) → verify visible again.

## Time-boxed implementation guidance

Target: **150–180 minutes**. Prioritize: (1) soft delete + default exclusion from reads, (2)
restore endpoint, (3) role gating. A minimal/naive auth mechanism (hardcoded-in-config demo
users, not hardcoded in code) is acceptable given the time box — document it clearly as a
placeholder for a real identity provider.

## Trade-offs and follow-up work

- Deferred: full user-account management (registration, password reset), integration with an
  external identity provider (OAuth2/OIDC), fine-grained permissions beyond a single `ADMIN`
  role.
- Consider adding an endpoint (admin-only) to list soft-deleted users if operational visibility
  is needed later.

## Quality requirements

- Authorization logic is centralized (security config / method security), not duplicated across
  controllers.
- No secrets or demo credentials committed as hard-coded strings in `.java` files — use
  externalized configuration with placeholder values documented in the README.
- No sensitive data (tokens, passwords) appears in logs.

## Key business rules out of controllers

- Soft-delete/restore semantics, the "already restored is a no-op" rule, and default exclusion
  of soft-deleted rows from reads live in the application/service layer and repository query
  definitions, not in `UserController`.

## Key persistent details out of API response

- `deletedAt` and any other soft-delete bookkeeping columns must not appear in `UserResponse`
  for standard endpoints; if an admin-only "show deleted" view is added, use a distinct response
  DTO rather than exposing the persistence column name directly.
