# User Management System

A local mono repo containing a full-stack user registration management system, built to
demonstrate clean architecture on the backend and a clean, responsive dashboard on the
frontend. This repo also hosts a set of scoped take-home assignments and their evaluation
criteria for developer interviews (see [assignments/](assignments/) and [evaluation/](evaluation/)).

## Repository layout

```
user-management/
├── backend/            Spring Boot 3.5.x (Java 21) API, clean architecture
├── frontend/            React + TypeScript dashboard (Vite, Jest, RTL)
├── docs/                 Repository documentation, backlog, interview problem statements
├── assignments/          Six scoped take-home assignments (Assignment 1-6)
└── evaluation/           Evaluation criteria for the assignments
```

## Technology stack

**Backend**
- Java 21, Spring Boot 3.5.x
- Spring Web, Spring Validation, Spring Data JPA
- H2 in-memory database
- JUnit 5, Mockito, Spring Boot Test
- Runs on **port 8080**

**Frontend**
- React 18 + TypeScript
- Vite dev server / build tool
- Jest + React Testing Library
- Runs on **port 5173**

## Local setup

### Prerequisites
- Java 21 (JDK)
- Maven 3.9+
- Node.js 18+ and npm

### Backend

```bash
cd backend
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. Sample endpoints:

- `POST   /api/users`
- `GET    /api/users`
- `GET    /api/users/{id}`
- `PUT    /api/users/{id}`
- `DELETE /api/users/{id}`

Run tests:

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The dashboard starts on `http://localhost:5173` and calls the backend at
`http://localhost:8080/api` (configurable via the `VITE_API_BASE_URL` environment variable).

Run tests and build:

```bash
cd frontend
npm test
npm run build
```

## Documentation

See [docs/repository-documentation.md](docs/repository-documentation.md) for a full description
of the existing system, its architecture, and the roadmap of future exercises (search, sort,
filter, CSV import/preview, authentication, soft delete/restore, audit history, health checks,
and structured logging).

Other documents:
- [docs/Backlog.md](docs/Backlog.md)
- [docs/Developer%20AIDLC%20interview%20problem%20statements.md](<docs/Developer AIDLC interview problem statements.md>)
- [evaluation/Evaluation%20criteria.md](<evaluation/Evaluation criteria.md>)
- [assignments/](assignments/) — Assignment 1 through 6
