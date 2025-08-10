variable "app_version" {
  description = "The version of the application to deploy"
  type        = string
}

variable "gcp_project_id" {
  description = "The GCP project ID where to deploy resources"
  type        = string
}

variable "gcp_region" {
  description = "The GCP region where to deploy resources"
  type        = string
}

variable "gcp_subnetwork_name" {
  description = "The name of the GCP subnetwork to use for the application"
  type        = string
}
