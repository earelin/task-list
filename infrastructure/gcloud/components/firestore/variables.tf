variable "gcp_project_id" {
  description = "The GCP project ID where to deploy resources"
  type        = string
}

variable "gcp_region" {
  description = "The GCP region where to deploy resources"
  type        = string
}

variable "service_account_email" {
  description = "The email of the service account to use for Firestore"
  type        = string
}
