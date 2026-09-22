# Assignment 1: Search, Sort, Filter, Empty

## User story

As a dashboard user managing many registered users, I want to search by name/email, sort the
list by any column, and filter by simple criteria, so that I can quickly find the records I
care about — and I want a clear empty state when no results match.

## Acceptance criteria

- `GET /api/users` accepts optional query parameters: `q` (free-text search across full name
  and email), `sortBy` (`fullName`, `email`, `createdAt`), `sortDir` (`asc`/`desc`), and can be
  extended with simple filters (e.g., `createdAfter`).
- Search is case-insensitive and matches partial strings in full name or email.
- Invalid `sortBy`/`sortDir` values return HTTP 400 with a validation error via the existing
  `ErrorResponse` envelope — never a 500.
- When no users match the search/filter criteria, the API returns HTTP 200 with an empty data
  array (not an error), and the frontend renders an explicit empty state ("No users match your
  search") distinct from the loading state and the "no users at all" state.
- Existing `GET /api/users` behavior (no query params) is unchanged and remains backward
  compatible.

## Constraints

- Do not introduce pagination in this assignment (out of scope) — return the full filtered/
  sorted list.
- Do not change the `UserResponse` shape.
- Keep query-parameter parsing/validation in the web layer, but keep the actual search/sort/
  filter **logic** in the application layer (a use case or a query object), not in the
  controller.

## Expected implementation slice

- Extend `ListUsersUseCase`/`ListUsersService` (or add a new `SearchUsersUseCase`) to accept a
  query object (search term, sort field, sort direction, optional filters).
- Extend `UserRepositoryPort` with a method that supports search/sort/filter, backed by a Spring
  Data JPA query (`Specification`, derived query methods, or `@Query`) in the adapter.
- Update `UserController` to parse query parameters into the query object and delegate.
- Frontend: add a search input, column-sort controls, and wire them to `GET /api/users` query
  parameters; add an explicit empty-results state in `UserTable`.

## Testing expectations

- Unit tests for the use case covering: no criteria (returns all), search match, search no
  match (empty list), each sort field/direction, invalid sort field handling.
- Repository/adapter test (e.g., `@DataJpaTest`) verifying the underlying query behaves
  correctly against H2.
- Controller test verifying query parameter parsing and 400 on invalid `sortBy`/`sortDir`.
- Frontend test verifying the empty-state message renders when the API returns an empty array
  distinct from initial loading.

## Time-boxed implementation guidance

Target: **90–120 minutes**. Prioritize: (1) search across name/email, (2) sort by one column,
(3) empty state. Treat additional filters as a stretch goal if time remains.

## Trade-offs and follow-up work

- Deferred: pagination, multi-field simultaneous sort, saved filter presets.
- Consider indexing `full_name`/`email` columns if this moves to a real database.
- Consider debouncing the search input on the frontend to reduce request volume.

## Quality requirements

- No N+1 queries; a single query should satisfy search + sort + filter.
- Query parameter validation errors use the existing `ErrorResponse` envelope.
- No business rule (e.g., what counts as a "match") lives in the controller.

## Key business rules out of controllers

- Matching rules (case-insensitivity, partial match fields), default sort order, and allowed
  sort fields/directions are defined and validated in the application layer, not in
  `UserController`.

## Key persistent details out of API response

- Any internal query mechanics (e.g., JPA `Specification` predicates, database-specific
  collation settings) must not be reflected in `UserResponse` or leak into error messages.
