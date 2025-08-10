variable "gcp_project_id" {
  description = "GCP project ID"
  type        = string
}

variable "gcp_region" {
  description = "Region (kept for symmetry, not strictly needed for IAM resources)"
  type        = string
}

variable "github_repository" {
  description = "GitHub repository in the form owner/repo"
  type        = string
}

variable "service_account_id" {
  description = "Service account ID (without domain) to be created for GitHub Actions"
  type        = string
  default     = "github-actions"
}

variable "sa_roles" {
  description = "List of IAM roles to bind to the service account"
  type        = list(string)
  default = [
    "roles/artifactregistry.writer",
    "roles/run.admin",
    "roles/iam.serviceAccountTokenCreator"
  ]
}

