variable "github_actions_service_account_email" {
  description = "GitHub Actions service account email (from shared env)"
  type        = string
  default     = null
}

variable "shared_subnetwork_name" {
  description = "Name of the shared subnetwork to attach resources"
  type        = string
}

variable "shared_network_name" {
  description = "Name of the shared VPC network (optional)"
  type        = string
  default     = null
}