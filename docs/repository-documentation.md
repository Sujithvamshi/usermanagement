# Repository Documentation

## 1. Purpose

This repository implements a **user registration management system** used as the baseline for
a series of scoped developer interview assignments. The current system (backend + frontend)
is fully functional, tested, and intentionally kept simple so that assignment work can be
layered on top of it without needing to first understand a large, unfamiliar codebase.

## 2. Current system overview

### 2.1 Capabilities (existing, correct behavior)

- Create a user (full name + email, email uniqueness enforced)
- Retrieve a single user by id
- List all users
- Update a user's full name / email (email uniqueness re-checked, excluding self)
- Delete a user
- Consistent success/error JSON envelopes on every endpoint
- Input validation (blank fields, invalid email, size limits) returns HTTP 400 with field-level
  details
- Duplicate email returns HTTP 409
- Missing user returns HTTP 404
- Dashboard UI to list, add, edit and delete users, including an empty state

### 2.2 Backend architecture (clean architecture)

```
com.example.usermanagement
├── domain/                 Framework-agnostic User model
├── application/
│   ├── usecase/             Use case interfaces + input commands
│   ├── service/              Use case implementations (business rules live here)
│   └── exception/            Application-level exceptions (NotFound, DuplicateEmail)
├── ports/                    UserRepositoryPort (outbound port, storage-agnostic)
├── adapters/
│   └── persistence/          UserJpaEntity, UserJpaRepository (Spring Data), adapter + mapper
└── web/
    ├── UserController.java   Thin HTTP adapter — no business rules
    ├── GlobalExceptionHandler.java
    ├── WebConfig.java         CORS configuration for local frontend
    └── dto/                   CreateUserRequest, UpdateUserRequest, UserResponse,
                                ApiResponse<T>, ErrorResponse
```

Key architectural rules already enforced in the codebase (keep these true for all new work):

1. **Business rules live in `application/service`, never in `web` controllers.** Controllers
   only map requests/responses and delegate to use cases.
2. **JPA entities never cross the API boundary.** `UserJpaEntity` stays inside
   `adapters/persistence`; the API only ever returns `UserResponse`, mapped from the domain
   `User` model.
3. **Persistence details (e.g., surrogate keys generation strategy, JPA annotations, Hibernate
   dirty-checking, `@PrePersist`/`@PreUpdate`) are private to the persistence adapter.** They
   must never be reflected in `web/dto` classes.
4. Dependency injection is constructor-based throughout; no field injection, no service
   locators.
5. Every endpoint returns the same envelope shape: `ApiResponse<T>` on success,
   `ErrorResponse` on failure.

### 2.3 Frontend architecture

- `src/api/userApi.ts` — a small typed fetch client, single source of truth for the API base
  URL and response unwrapping/error mapping.
- `src/components/DashboardLayout.tsx` — sidebar + content shell for a management dashboard
  look and feel.
- `src/components/UserForm.tsx`, `UserTable.tsx` — create/edit form and list view, including
  an empty state.
- `src/App.tsx` — wires data loading, create/update/delete flows, and error banners.

### 2.4 Testing baseline

- Backend: Mockito-based unit tests per use case service, a `@WebMvcTest` controller test, a
  full `@SpringBootTest` + `MockMvc` integration test exercising the H2-backed lifecycle, and
  an application context smoke test.
- Frontend: Jest + React Testing Library component tests (`UserForm`, `UserTable`) and an
  `App` test that mocks the API client.

## 3. Roadmap: future exercises

The assignments in [../assignments/](../assignments/) describe scoped, self-contained slices of
work to be implemented **on top of** the existing system without breaking existing behavior.
At a high level, the roadmap covers:

| Area | Assignment |
|---|---|
| Search, sort, filter, and empty-state handling on the user list | Assignment 1 |
| CSV bulk-import with a preview step before committing | Assignment 2 |
| Authentication + role-based access control for soft delete / restore | Assignment 3 |
| Audit history of changes to a user record | Assignment 4 |
| Health check endpoint for operational readiness | Assignment 5 |
| Structured (JSON) logging with correlation ids | Assignment 6 |

Each assignment is written as an independent, time-boxed exercise (see
[evaluation/Evaluation criteria.md](<../evaluation/Evaluation criteria.md>) for how submissions
are scored) and each explicitly requires:

- Business rules to stay out of controllers (application/service layer only)
- Persistence-only details (soft-delete flags, audit columns, row versions, etc.) to stay out
  of API responses unless the assignment explicitly asks for them to be surfaced
- Backward compatibility with the existing endpoints and response envelopes
- No hard-coded secrets, and no logging of passwords, tokens, or other sensitive data

## 4. Local development

See the root [README.md](../README.md) for setup and run instructions for both backend and
frontend.
