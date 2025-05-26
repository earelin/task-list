variable "gcp_project_id" {
  type    = string
  description = "The Google Cloud project ID where the secret will be created."
}

variable "gcp_region" {
  type        = string
  description = "The Google Cloud region where the secret will be stored."
}

variable "label" {
  type        = string
  description = "A label for the secret, used for identification."
}

variable "secret_id" {
  type        = string
  description = "The ID of the secret to create in Google Secret Manager."
}

variable "value" {
  type        = string
  description = "The value to store in the secret."
  default = ""
}
