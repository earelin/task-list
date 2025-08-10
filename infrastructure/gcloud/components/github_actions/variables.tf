variable "gcp_project_id" {
  description = "The GCP project ID where to deploy resources"
  type        = string
}

variable "service_account_id" {
  description = "Service account ID (without domain) to be created for GitHub Actions"
  type        = string
  default     = "github-actions"
}
