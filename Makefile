.PHONY: lint lint-dockerfiles lint-pipelines lint-terraform

lint: lint-dockerfiles lint-pipelines lint-terraform

lint-dockerfiles:
	@echo "Linting Dockerfiles..."
	@checkov --directory app \
		--file app/Dockerfile \
		--framework dockerfile --quiet \
		--summary-position bottom
	@hadolint app/Dockerfile

lint-pipelines:
	@echo "Linting pipelines..."
	@checkov --directory .github/workflows \
		--framework github_actions --quiet \
		--summary-position bottom

lint-terraform:
	@echo "Linting Terraform files..."
	@terraform fmt -check -diff -recursive infrastructure
	@checkov --directory infrastructure \
 		--framework terraform --quiet \
 		--skip-check CKV_TF_1 \
		--skip-check CKV_TF_2 \
		--summary-position bottom

fix-lint-terraform:
	@echo "Fixing Terraform files..."
	@terraform fmt -recursive infrastructure
