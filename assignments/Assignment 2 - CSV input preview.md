# Assignment 2: CSV Input Preview

## User story

As a dashboard user, I want to upload a CSV file of users and see a preview of what will be
imported (including which rows are valid and which have errors) before anything is committed,
so that I can fix problems or cancel without polluting the database.

## Acceptance criteria

- `POST /api/users/import/preview` accepts a CSV file (multipart or raw text) and returns a
  preview response containing: total rows parsed, count of valid rows, count of invalid rows,
  and a per-row breakdown (row number, parsed fields, validation errors if any). **Nothing is
  persisted** by this endpoint.
- `POST /api/users/import/commit` accepts the same CSV (or a reference/token from the preview
  step) and persists only the valid rows, returning a summary (created count, skipped count,
  per-row outcome). Duplicate emails within the file or against existing users are reported as
  row-level errors, not fatal failures.
- Malformed rows (missing required fields, invalid email format, exceeding size limits) do not
  abort the whole import — they are reported individually.
- A CSV with zero valid rows still returns a successful preview response with an empty valid
  set and full error detail (not an HTTP error).
- Frontend shows a preview table (valid rows highlighted, invalid rows flagged with reasons) and
  a confirm/cancel action before committing.

## Constraints

- Reuse existing validation rules from `CreateUserRequest` (full name required, email required
  and valid, size limits) rather than duplicating them.
- Do not accept arbitrarily large files without a sane limit (e.g., reject files over a
  configurable max size/row count with a clear error).
- Do not persist anything during the preview step, under any circumstance.

## Expected implementation slice

- Add a CSV parsing utility in the application layer (e.g., `application/csv`) that produces a
  list of row results (parsed data + validation errors), independent of HTTP concerns.
- Add `PreviewImportUseCase` and `CommitImportUseCase` (or a single `ImportUsersUseCase` with
  two modes) that reuse `CreateUserCommand` validation semantics.
- Add controller endpoints for preview and commit, with request/response DTOs
  (`ImportPreviewResponse`, `ImportCommitResponse`) — never expose parsing internals.
- Frontend: file upload control, preview table, confirm/cancel buttons.

## Testing expectations

- Unit tests for the CSV parser: well-formed file, missing columns, extra whitespace, invalid
  email, duplicate email within file, empty file.
- Unit tests for preview use case confirming no repository writes occur.
- Unit/integration tests for commit use case confirming only valid rows are persisted and
  duplicates (in-file and against existing data) are reported, not silently dropped or fatal.
- Controller test for both endpoints, including the "zero valid rows" success case.

## Time-boxed implementation guidance

Target: **120–150 minutes**. Prioritize: (1) parsing + validation + preview endpoint, (2) commit
endpoint that persists only valid rows. Treat the polished frontend preview UI as a stretch
goal if time is short — a minimal table is sufficient.

## Trade-offs and follow-up work

- Deferred: resumable/streaming uploads for very large files, background job processing with
  progress polling, configurable column mapping.
- Consider storing the preview result server-side (keyed by a token) instead of re-sending the
  full CSV on commit, once file sizes grow.

## Quality requirements

- Parsing logic is unit-testable without Spring/HTTP context.
- Preview endpoint has no side effects (verified by test).
- Clear, actionable per-row error messages (e.g., "row 4: email is not valid").

## Key business rules out of controllers

- CSV parsing, per-row validation, and duplicate detection rules live in the application layer,
  not in the controller, and are reused from existing user-creation validation where possible.

## Key persistent details out of API response

- Internal representations used during parsing (e.g., temporary staging entities, file storage
  paths) must never appear in `ImportPreviewResponse`/`ImportCommitResponse`.
