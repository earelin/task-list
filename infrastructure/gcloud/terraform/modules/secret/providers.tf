provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.36.1"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.7.2"
    }
  }
}
