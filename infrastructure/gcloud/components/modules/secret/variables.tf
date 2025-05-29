variable "gcp_project_id" {
  type        = string
  description = "The Google Cloud project ID where the secret will be created."
}

variable "gcp_region" {
  type        = string
  description = "The Google Cloud region where the secret will be stored."
}

variable "name" {
  type        = string
  description = "The name of the secret to create in Google Secret Manager."
}

variable "value" {
  type        = string
  description = "The value to store in the secret."
  default     = ""
}
