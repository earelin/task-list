variable "gcp_project_id" {
  description = "The GCP project ID where to deploy resources"
  type        = string
}

variable "gcp_region" {
  description = "The GCP region where to deploy resources"
  type        = string
}

variable "service_account_id" {
  description = "Service account ID (without domain) to be created for GitHub Actions"
  type        = string
  default     = "github-actions"
}

variable "task_list_repository_name" {
  description = "Name of the Artifact Registry repository for task list images"
  type        = string
}
