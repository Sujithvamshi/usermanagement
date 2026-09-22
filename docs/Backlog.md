# Backlog

Backlog of future work for the user registration management system, grouped by theme. Items
map to the assignments in [../assignments/](../assignments/) where a scoped exercise already
exists.

## Now (baseline — already implemented)

- [x] Create / read / update / delete a user
- [x] Email uniqueness validation
- [x] Field validation with consistent error responses
- [x] Clean architecture layering (domain / application / ports / adapters / web)
- [x] Dashboard UI (list, create, edit, delete, empty state)
- [x] Backend unit + integration tests, frontend component tests

## Next

- [ ] **Search, sort, filter, and empty results** on the user list (Assignment 1)
- [ ] **CSV input with preview** before committing a bulk import (Assignment 2)
- [ ] **Authentication + RBAC** gating soft delete / restore actions (Assignment 3)
- [ ] **Audit history** of user record changes (Assignment 4)
- [ ] **Health check endpoint** (`/actuator/health` or custom) (Assignment 5)
- [ ] **Structured logging** with request correlation ids (Assignment 6)

## Later (not yet scoped)

- [ ] Pagination for large user lists
- [ ] Optimistic concurrency (row versioning) surfaced safely to clients
- [ ] Password-based authentication with hashed credentials (if user accounts are introduced)
- [ ] Rate limiting on write endpoints
- [ ] Externalized configuration per environment (dev/test/prod profiles)

## Non-goals for this baseline

- No production datastore (H2 in-memory is intentional for local development and interviews)
- No UI theming/branding beyond a clean, responsive dashboard layout
- No multi-tenant support
