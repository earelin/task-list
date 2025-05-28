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
	@checkov --directory infrastructure \
 		--framework terraform --quiet \
		--skip-check CKV_GCP_26 \
		--skip-check CKV_GCP_32 \
		--skip-check CKV_GCP_38 \
		--skip-check CKV_GCP_39 \
 		--skip-check CKV_GCP_84 \
 		--skip-check CKV_TF_1 \
		--skip-check CKV_TF_2 \
		--summary-position bottom
