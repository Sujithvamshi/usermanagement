# Developer AIDLC Interview Problem Statements

This document frames the six assignments in [../assignments/](../assignments/) as an
AI-assisted development lifecycle (AIDLC) interview exercise. Candidates are given the existing
user registration management system (already implemented and tested) and asked to implement
**one** assignment within a fixed time box, using AI coding assistants as they normally would.

## Why this format

- The baseline system is small enough to understand in minutes but real enough (clean
  architecture, layered tests, typed frontend) to expose how a candidate works within existing
  conventions rather than greenfield code.
- Each assignment isolates a distinct engineering skill: data-shape design (search/sort/filter),
  input handling and UX (CSV preview), security (authN/RBAC), data modeling for history
  (audit), operational readiness (health check), and observability (structured logging).
- Grading is objective and repeatable — see
  [../evaluation/Evaluation criteria.md](<../evaluation/Evaluation criteria.md>).

## How the interview is run

1. Candidate is given repository access (or a zip) with the baseline system pre-built and
   passing tests.
2. Candidate picks (or is assigned) one assignment from
   [../assignments/](../assignments/).
3. Candidate has the time-boxed duration specified in that assignment's
   **Time-boxed implementation guidance** section to implement the **Expected implementation
   slice**.
4. Candidate may use AI coding assistants throughout, consistent with normal day-to-day
   workflow. Interviewers should observe how prompts are framed, how generated code is
   reviewed, and how the candidate validates behavior (tests, manual checks).
5. At the end of the time box, the candidate presents:
   - What was implemented vs. deferred, and why
   - How it was tested
   - Trade-offs made under the time constraint
6. Evaluators score against the assignment's **Acceptance criteria** and **Quality
   requirements**, using [../evaluation/Evaluation criteria.md](<../evaluation/Evaluation criteria.md>).

## Ground rules for every assignment

- Do not break existing endpoints, response shapes, or tests — treat the baseline as a
  contract.
- Keep business rules out of controllers; keep persistence-only details out of API responses.
- Use dependency injection; avoid introducing abstractions not needed for the assignment.
- Never hard-code secrets; never log passwords, tokens, or other sensitive data.
- Prefer small, reviewable diffs over rewriting existing modules.

## Assignments index

1. [Assignment 1: Search, sort, filter, empty](<../assignments/Assignment 1 - Search, sort, filter, empty.md>)
2. [Assignment 2: CSV input preview](<../assignments/Assignment 2 - CSV input preview.md>)
3. [Assignment 3: Authentication RBAC for soft delete/restore](<../assignments/Assignment 3 - Authentication RBAC for soft delete-restore.md>)
4. [Assignment 4: Audit history](<../assignments/Assignment 4 - Audit history.md>)
5. [Assignment 5: Health check](<../assignments/Assignment 5 - Health check.md>)
6. [Assignment 6: Structured logging](<../assignments/Assignment 6 - Structured logging.md>)
