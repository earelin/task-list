variable "app_version" {
  type = string
}

variable "gcp_project_id" {
  type = string
}

variable "gcp_region" {
  type    = string
  default = "europe-southwest1"
}

variable "mongodb_version" {
  type    = string
  default = "8.0.9"
}
