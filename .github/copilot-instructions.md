## Task List – AI Assistant Working Guide

Focus: Java 21 multi-module Gradle (Spring Boot app + test modules + Terraform infra). Optimize for fast, correct edits with existing patterns.

### High-Level Architecture

- Modules: `app/` (Spring Boot), `acceptance-test/` (Cucumber), `performance-test/` (Gatling), `infrastructure/` (Terraform: local + GCP).
- Persistence: Firestore (emulator locally). Domain-driven layering inside `app` (domain vs application vs infrastructure).
- Observability: OpenTelemetry (traces -> Jaeger, metrics -> Prometheus). Docker Compose profiles enable optional stacks.

### Core Code Conventions (App)

- Packages (indicative):
	- `domain` → entities, value objects, domain services; keep pure (no Spring annotations unless absolutely needed).
	- `application.rest` → controllers + DTO mapping (MapStruct). Keep mapping in dedicated mapper interfaces.
	- `application.security` → all security config centralised; avoid scattering filters.
	- `infrastructure.firestore` → Firestore adapters (repositories). Keep external model mapping isolated.
- Use Lombok for boilerplate; imitate existing annotations style in new classes.
- DTO <-> domain via MapStruct mappers; do NOT hand-write conversions unless unavoidable.
- Prefer constructor injection; no field injection.

### Build & Quality Workflow

- Typical rapid loop: `./gradlew :app:bootRun` (or use Docker Compose profile to start dependencies) → edit → hot reload (Spring DevTools if present).
- Full verification: `./gradlew build` (runs tests, static analysis via aggregated `check`).
- Targeted tasks:
	- Unit tests: `./gradlew test`
	- Integration tests: `./gradlew :app:integrationTest`
	- Acceptance (Cucumber): `./gradlew cucumber` (uses REST calls against running app or spins up context).
	- Performance: `./gradlew gatlingRun`
	- Static analysis: `./gradlew checkstyleMain spotbugsMain`
	- Sonar (if configured in CI): keep Jacoco instrumentation intact; avoid excluding new code.

### Testing Patterns

- Place unit tests in `app/src/test/java` mirroring package path.
- Integration tests in `app/src/integrationTest/java`; annotated with `@SpringBootTest` or slice tests; use Firestore emulator (port 8100) or testcontainers (follow existing pattern if present).
- Acceptance tests (Cucumber) define feature files + step defs (look at `acceptance-test` for naming). Reuse REST Assured config utilities.
- Performance tests: create new Gatling simulations under `performance-test/src/gatling`.

### Infrastructure & Local Env

- Local Kubernetes Terraform under `infrastructure/local/` — components linked via stack HCL files (`components.tfstack.hcl`). Firestore emulator deployment + ClusterIP service exposes port 8100 cluster-internally.
- GCP environment modules under `infrastructure/gcloud/components/*`; environments (e.g. `environments/dev`) instantiate modules and define backend + provider.
- Network module (already used in dev) provisions VPC, subnet, firewall basics. Extend by adding new firewall rules inside `components/network` instead of ad‑hoc rules elsewhere.
- Use `make lint-terraform` before committing infra changes; run `make fix-lint-terraform` to auto-format.

### When Modifying Infra

- Keep provider aliases consistent (`provider "kubernetes" "this"` in local stack files). Do not remove required two-label form in stack context.
- For new Kubernetes workloads: add Deployment + Service; consider readinessProbe mirroring livenessProbe; label with `app`, `component`, and reuse existing selector pattern (`test = "FirestoreEmulator"` shows current minimal example—prefer enriching labels rather than changing existing selectors abruptly).
- For GCP resources: add variables in module `variables.tf`, surface outputs if they will be consumed cross-module.

### Cross-Cutting Concerns

- Logging: follow existing logging configuration (look for `application.yml` or logging config before adding custom log frameworks).
- Error handling: centralize in a controller advice (check if present) before adding per-endpoint logic.
- Mapping & Validation: Prefer Bean Validation annotations on DTOs; enforce invariants in domain constructors.

### Adding New Features (Example Workflow)

1. Define/extend domain model (pure Java, no Spring).
2. Add repository adapter or extend Firestore repository layer.
3. Create MapStruct mapper updates for new fields.
4. Add REST endpoint (controller + request/response DTOs).
5. Add unit tests (domain + mapper), integration test (repository/service), acceptance scenario (feature file) if user-visible.
6. Run `./gradlew build` and fix style/SpotBugs issues before commit.

### Common Pitfalls to Avoid

- Introducing direct Firestore SDK calls inside controllers; keep them in infra adapter layer.
- Mixing domain and persistence models; always map.
- Bypassing MapStruct (maintain consistency, easier maintenance & Sonar compliance).
- Hardcoding ports; use configuration or environment variables where pattern exists.
- Terraform: altering existing selector labels without migrating dependent Services.

### Ask the Assistant To… (Good Prompts)

- "Add a new domain value object X with validation Y and expose via REST endpoint Z." 
- "Create a repository method to query tasks by status and add integration + acceptance tests." 
- "Extend network Terraform module to add firewall rule for port #### and propagate output." 
- "Introduce readiness probe for existing deployment matching health semantics." 

Keep responses concise, implement directly when files are clear, and run/adjust Gradle or Terraform tasks as needed.
